package com.tup.textilapp.config;

import com.tup.textilapp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tup.textilapp.model.entity.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleConfig implements CommandLineRunner {

    private final RoleRepository repository;

    public RoleConfig(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Role> list = new ArrayList<>();
        list.add(new Role(null, "ADMIN"));
        list.add(new Role(null, "CLIENT"));
        repository.saveAll(list);
    }
}
