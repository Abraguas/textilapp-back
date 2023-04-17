package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.JwtDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.UserEntity;
import com.tup.textilapp.service.UserService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/role")
    public ResponseEntity<?> getRole(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization").substring(7);
            String role = this.userService.getRoleByToken(token);
            return ResponseEntity.ok(new JwtDTO(role));
        } catch (MalformedJwtException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO("Invalid Jwt string"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }

    }
    @GetMapping(path = "/self")
    public ResponseEntity<?> getSelf(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization").substring(7);
            UserEntity user = this.userService.getUserByToken(token);
            return ResponseEntity.ok(user);
        } catch (MalformedJwtException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO("Invalid Jwt string"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }

    }
    @PostMapping(path = "/client")
    public ResponseEntity<?> registerClient(@RequestBody UserEntity user) {
        try {
            this.userService.registerClient(user);
            return ResponseEntity.ok(new ResponseMessageDTO("User registered succesfully" ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @PutMapping(path = "/self")
    public ResponseEntity<?> updateSelf(HttpServletRequest request, @RequestBody UserEntity user) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            this.userService.updateUser(token,user);
            return ResponseEntity.ok(new ResponseMessageDTO("User updated succesfully" ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
}
