package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.JwtDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.UserEntity;
import com.tup.textilapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/role")
    public ResponseEntity<?> getRole(HttpServletRequest request) {
            String token = request.getHeader("Authorization").substring(7);
            String role = this.userService.getRoleByToken(token);
            return ResponseEntity.ok(new JwtDTO(role));
    }

    @GetMapping(path = "/self")
    public ResponseEntity<?> getSelf(HttpServletRequest request) {
            String token = request.getHeader("Authorization").substring(7);
            UserEntity user = this.userService.getUserByToken(token);
            return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String searchString
    ) {
            if (searchString == null || searchString.length() < 1) {
                return ResponseEntity.ok(this.userService.getAll());
            }
            return ResponseEntity.ok(userService.searchByUserame(searchString));
    }

    @GetMapping(path = "/ranking")
    public ResponseEntity<?> getUsersRanking(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate) {
            return ResponseEntity.ok(this.userService.getUsersRanking(startDate, endDate));
    }

    @PostMapping(path = "/client")
    public ResponseEntity<?> registerClient(@RequestBody UserEntity user) {
            this.userService.registerClient(user);
            return ResponseEntity.ok(new ResponseMessageDTO("User registered succesfully"));
    }

    @PutMapping(path = "/self")
    public ResponseEntity<?> updateSelf(HttpServletRequest request, @RequestBody UserEntity user) {
            String token = request.getHeader("Authorization").substring(7);
            this.userService.updateUser(token, user);
            return ResponseEntity.ok(new ResponseMessageDTO("User updated succesfully"));
    }
}
