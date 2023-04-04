package com.tup.textilapp.config;

import com.tup.textilapp.model.entity.Brand;
import com.tup.textilapp.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandConfig implements CommandLineRunner {

    private final BrandRepository repository;
    @Value("${data.initialized}")
    private boolean dataInitialized;
    public BrandConfig(BrandRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (dataInitialized) {
            return;
        }

        List<Brand> list = new ArrayList<>();
        list.add(new Brand(null, "Arco Iris"));
        list.add(new Brand(null, "Brecia"));
        list.add(new Brand(null, "Danubio"));
        list.add(new Brand(null, "Eliplast"));
        list.add(new Brand(null, "Piñata"));
        repository.saveAll(list);

    }
}

