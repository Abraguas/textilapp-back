package com.tup.textilapp.controller;

import com.tup.textilapp.model.entity.Unit;
import com.tup.textilapp.repository.UnitRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "unit")
public class UnitController {
    private final UnitRepository unitRepository;

    public UnitController (UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @GetMapping
    public List<Unit> getAll() {
        return this.unitRepository.findAll();
    }
}