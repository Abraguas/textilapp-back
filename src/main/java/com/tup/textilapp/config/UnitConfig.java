package com.tup.textilapp.config;

import com.tup.textilapp.model.entity.Unit;
import com.tup.textilapp.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UnitConfig implements CommandLineRunner {

    private final UnitRepository repository;
    @Value("${data.initialized}")
    private boolean dataInitialized;

    public UnitConfig(UnitRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (dataInitialized) {
            return;
        }
        List<Unit> list = new ArrayList<>();
        list.add(new Unit(null, "Metro"));
        list.add(new Unit(null, "Kilo"));
        list.add(new Unit(null, "Unidad"));
        repository.saveAll(list);

    }
}