package com.tup.textilapp.service;

import com.tup.textilapp.model.entity.UserEntity;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.tup.textilapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        log.info("Consultara username " + username + " en base de datos");
        UserEntity userEntity = userRepository.findByUsername(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole().getName()));

        log.info("Usuario autenticado: " + username);

        return new User(username, userEntity.getPassword(), true, true, true, true, authorities);
    }

    public String getRoleByToken(String token) throws MalformedJwtException {
        String username = this.jwtService.extractClaimUsername(token);
        UserEntity user = this.userRepository.findByUsername(username);
        return user.getRole().getName();
    }
}
