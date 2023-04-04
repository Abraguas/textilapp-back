package com.tup.textilapp.controller;

import com.tup.textilapp.model.entity.Product;
import com.tup.textilapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Product product) {
        try {
            this.productService.save(product);
            return ResponseEntity.ok("Product created succesfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Product> getAll() {
        return this.productService.getAll();
    }
    @GetMapping(path = "{subCategoryId}")
    public ResponseEntity<?> getAllBySubCategory(@PathVariable Integer subCategoryId) {
        try {
            List<Product> lst = this.productService.getAllBySubCategory(subCategoryId);
            return ResponseEntity.ok(lst);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
