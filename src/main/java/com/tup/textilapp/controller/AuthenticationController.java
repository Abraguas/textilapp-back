package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.JwtDTO;
import com.tup.textilapp.model.entity.UserEntity;
import com.tup.textilapp.service.IJwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final IJwtService jwtService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            IJwtService iJwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = iJwtService;
        this.userDetailsService = userDetailsService;
    }


    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserEntity user) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        }catch (BadCredentialsException e) {
            throw new Exception("Incorrect", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtDTO(jwt));
    }
}

