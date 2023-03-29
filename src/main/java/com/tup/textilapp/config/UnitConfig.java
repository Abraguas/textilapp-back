package com.tup.textilapp.config;

import com.tup.textilapp.model.entity.Role;
import com.tup.textilapp.model.entity.Unit;
import com.tup.textilapp.repository.RoleRepository;
import com.tup.textilapp.repository.UnitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UnitConfig implements CommandLineRunner {

    private final UnitRepository repository;

    public UnitConfig(UnitRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Unit> list = new ArrayList<>();
        list.add(new Unit(null, "Metros"));
        list.add(new Unit(null, "Kilos"));
        list.add(new Unit(null, "Unidades"));
        repository.saveAll(list);
    }
}