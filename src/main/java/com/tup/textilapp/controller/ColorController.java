package com.tup.textilapp.controller;

import com.tup.textilapp.model.entity.Color;
import com.tup.textilapp.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "color")
public class ColorController {
    private final ColorRepository colorRepository;

    @Autowired
    public ColorController (ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @GetMapping
    public List<Color> getAll() {
        return this.colorRepository.findAll();
    }
}
