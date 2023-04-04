package com.tup.textilapp.config;

import com.tup.textilapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import com.tup.textilapp.model.entity.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleConfig implements CommandLineRunner {

    private final RoleRepository repository;
    @Value("${data.initialized}")
    private boolean dataInitialized;
    public RoleConfig(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (dataInitialized) {
            return;
        }

        List<Role> list = new ArrayList<>();
        list.add(new Role(null, "ADMIN"));
        list.add(new Role(null, "CLIENT"));
        repository.saveAll(list);

    }
}
