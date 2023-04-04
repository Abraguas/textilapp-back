package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.CategoryDTO;
import com.tup.textilapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController( CategoryService categoryService ) { this.categoryService = categoryService; }

    @GetMapping
    public List<CategoryDTO> getAll() {
        return this.categoryService.getAll();
    }
}
