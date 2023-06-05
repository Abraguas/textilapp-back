package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.CategoryDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.Category;
import com.tup.textilapp.model.entity.SubCategory;
import com.tup.textilapp.service.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> getAll() {
        return this.categoryService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {

        this.categoryService.saveCategory(category);
        return ResponseEntity.ok(new ResponseMessageDTO("Registered Category succesfully"));

    }

    @PostMapping(path = "subCategory")
    public ResponseEntity<?> saveSubCategory(@RequestBody SubCategory subCategory) {

        this.categoryService.saveSubcategory(subCategory);
        return ResponseEntity.ok(new ResponseMessageDTO("Registered SubCategory succesfully"));

    }

    @DeleteMapping(path = "{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId) {

        this.categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new ResponseMessageDTO("Category deleted succesfully"));

    }

    @DeleteMapping(path = "subCategory/{subCategoryId}")
    public ResponseEntity<?> deleteSubCategory(@PathVariable Integer subCategoryId) {

        this.categoryService.deleteSubcategory(subCategoryId);
        return ResponseEntity.ok(new ResponseMessageDTO("Subcategory deleted succesfully"));

    }

    @PutMapping(path = "{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId, @RequestBody Category category) {

        this.categoryService.updateCategory(category, categoryId);
        return ResponseEntity.ok(new ResponseMessageDTO("Category updated succesfully"));

    }

    @PutMapping(path = "subCategory/{subCategoryId}")
    public ResponseEntity<?> updateSubCategory(@PathVariable Integer subCategoryId, @RequestBody SubCategory subCategory) {

        this.categoryService.updateSubCategory(subCategory, subCategoryId);
        return ResponseEntity.ok(new ResponseMessageDTO("SubCategory updated succesfully"));

    }
}
