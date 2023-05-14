package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.CategoryDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.Category;
import com.tup.textilapp.model.entity.SubCategory;
import com.tup.textilapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {
        try {
            this.categoryService.saveCategory(category);
            return ResponseEntity.ok("Registered Category succesfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @PostMapping(path = "subCategory")
    public ResponseEntity<?> saveSubCategory(@RequestBody SubCategory subCategory) {
        try {
            this.categoryService.saveSubcategory(subCategory);
            return ResponseEntity.ok("Registered SubCategory succesfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @DeleteMapping(path = "{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId) {
        try {
            this.categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(new ResponseMessageDTO("Category deleted succesfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @DeleteMapping(path = "subCategory/{subCategoryId}")
    public ResponseEntity<?> deleteSubCategory(@PathVariable Integer subCategoryId) {
        try {
            this.categoryService.deleteSubcategory(subCategoryId);
            return ResponseEntity.ok(new ResponseMessageDTO("Subcategory deleted succesfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @PutMapping(path = "{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId, @RequestBody Category category) {
        try {
            this.categoryService.updateCategory(category, categoryId);
            return ResponseEntity.ok(new ResponseMessageDTO("Category updated succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @PutMapping(path = "subCategory/{subCategoryId}")
    public ResponseEntity<?> updateSubCategory(@PathVariable Integer subCategoryId, @RequestBody SubCategory subCategory) {
        try {
            this.categoryService.updateSubCategory(subCategory, subCategoryId);
            return ResponseEntity.ok(new ResponseMessageDTO("SubCategory updated succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
}
