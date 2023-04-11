package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.JwtDTO;
import com.tup.textilapp.service.UserService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return ResponseEntity.badRequest().body("Invalid Jwt string");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
