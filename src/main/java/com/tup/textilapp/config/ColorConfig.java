package com.tup.textilapp.config;

import com.tup.textilapp.model.entity.Color;
import com.tup.textilapp.repository.ColorRepository;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ColorConfig implements CommandLineRunner {
    private final ColorRepository repository;

    public ColorConfig(ColorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        List<Color> list = new ArrayList<>();
        list.add(new Color(null,"Rojos/Bordós"));
        list.add(new Color(null,"Amarillos/Mostazas"));
        list.add(new Color(null,"Beiges/Visones"));
        list.add(new Color(null,"Azules"));
        list.add(new Color(null,"Blancos"));
        list.add(new Color(null,"Celestes"));
        list.add(new Color(null,"Dorados"));
        list.add(new Color(null,"Fucsias"));
        list.add(new Color(null,"Grises"));
        list.add(new Color(null,"Marrones"));
        list.add(new Color(null,"Melange"));
        list.add(new Color(null,"Multicolor"));
        list.add(new Color(null,"Naranjas/Terracotas"));
        list.add(new Color(null,"Naturales"));
        list.add(new Color(null,"Negro/Crudo"));
        list.add(new Color(null,"Negros"));
        list.add(new Color(null,"Petróleos"));
        list.add(new Color(null,"Rosas"));
        list.add(new Color(null,"Surtidos"));
        list.add(new Color(null,"Transparente"));
        list.add(new Color(null,"Turquesas"));
        list.add(new Color(null,"Verdes"));
        list.add(new Color(null,"Violetas"));
        repository.saveAll(list);
    }
}
