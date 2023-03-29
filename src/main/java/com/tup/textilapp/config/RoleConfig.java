package com.tup.textilapp.config;

import com.tup.textilapp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tup.textilapp.model.entity.Role;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RoleConfig {
    @Bean
    CommandLineRunner commandLineRunner(RoleRepository repository){
        return args -> {
            List<Role> list = new ArrayList<>();
            list.add(new Role(null,"ADMIN"));
            list.add(new Role(null,"CLIENT"));
            repository.saveAll(list);
        };
    }
}
