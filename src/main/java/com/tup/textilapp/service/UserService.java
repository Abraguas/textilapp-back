package com.tup.textilapp.service;

import com.tup.textilapp.model.dto.GetUserDTO;
import com.tup.textilapp.model.dto.UserRankingDTO;
import com.tup.textilapp.model.entity.Role;
import com.tup.textilapp.model.entity.UserEntity;
import com.tup.textilapp.repository.RoleRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        log.info("Consultara username " + username + " en base de datos");
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("User doesn't exist"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole().getName()));

        log.info("Usuario autenticado: " + username);

        return new User(username, userEntity.getPassword(), true, true, true, true, authorities);
    }

    public String getRoleByToken(String token) throws MalformedJwtException {
        return getUserByToken(token).getRole().getName();
    }
    public List<GetUserDTO> getAll()  {
        return this.userRepository.findAll().stream().map(u -> new GetUserDTO(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getName(),
                u.getLastname(),
                u.getPhonenumber()
        )).toList();
    }
    public List<GetUserDTO> searchByUserame(String name) {
        return this.userRepository.findAllByUsernameContainingIgnoreCase(name).stream().map(u -> new GetUserDTO(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getName(),
                u.getLastname(),
                u.getPhonenumber()
        )).toList();
    }
    public List<UserRankingDTO> getUsersRanking() {
        return this.userRepository.getUsersTotalMoneySpent();
    }
    public UserEntity getUserByToken(String token) throws MalformedJwtException {
        String username = this.jwtService.extractClaimUsername(token);
        return this.userRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("User doesn't exist"));
    }

    public void registerClient(UserEntity user) {
        if(this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username taken");
        }
        if(this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email taken");
        }
        Role role = this.roleRepository.findByName("CLIENT");
        user.setRole(role);
        this.userRepository.save(user);
    }
    @Transactional
    public void updateUser(String token, UserEntity newUser) {
        String username = this.jwtService.extractClaimUsername(token);
        UserEntity user = this.userRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("User doesn't exist"));
        if (newUser.getUsername() != null
                && !newUser.getUsername().equals(user.getUsername())
                && newUser.getUsername().length() > 0) {
            if(this.userRepository.findByUsername(newUser.getUsername()).isPresent()) {
                throw new IllegalStateException("Username taken");
            }
            user.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword() != null
                && !newUser.getPassword().equals(user.getPassword())
                && newUser.getPassword().length() > 0) {
            user.setPassword(newUser.getPassword());
        }
        if (newUser.getName() != null
                && !newUser.getName().equals(user.getName())
                && newUser.getName().length() > 0) {
            user.setName(newUser.getName());
        }
        if (newUser.getLastname() != null
                && !newUser.getLastname().equals(user.getLastname())
                && newUser.getLastname().length() > 0) {
            user.setLastname(newUser.getLastname());
        }
        if (newUser.getEmail() != null
                && !newUser.getEmail().equals(user.getEmail())
                && newUser.getEmail().length() > 0) {
            if(this.userRepository.findByEmail(newUser.getEmail()).isPresent()) {
                throw new IllegalStateException("Email taken");
            }
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getPhonenumber() != null
                && !newUser.getPhonenumber().equals(user.getPhonenumber())
                && newUser.getPhonenumber().length() > 0) {
            user.setPhonenumber(newUser.getPhonenumber());
        }
    }
}
