package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.Product;
import com.tup.textilapp.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Product product) {
        this.productService.save(product);
        return ResponseEntity.ok(new ResponseMessageDTO("Product created succesfully"));
    }

    @PutMapping(path = "unlist/{productId}")
    public ResponseEntity<?> unlist(@PathVariable Integer productId) {
        this.productService.unlist(productId);
        return ResponseEntity.ok(new ResponseMessageDTO("Product unlisted succesfully"));
    }

    @PutMapping(path = "list/{productId}")
    public ResponseEntity<?> list(@PathVariable Integer productId) {
        this.productService.list(productId);
        return ResponseEntity.ok(new ResponseMessageDTO("Product listed succesfully"));
    }

    @PutMapping(path = "{productId}")
    public ResponseEntity<?> update(@PathVariable Integer productId, @RequestBody Product product) {
        this.productService.update(productId, product);
        return ResponseEntity.ok(new ResponseMessageDTO("Product updated succesfully"));
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<?> getById(@PathVariable Integer productId) {
        return ResponseEntity.ok(this.productService.getById(productId));
    }

    @GetMapping(path = "all")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String searchString
    ) {
        if (searchString == null || searchString.length() < 1) {
            return ResponseEntity.ok(this.productService.getAll());
        }
        return ResponseEntity.ok(productService.searchByName(searchString));
    }

    @GetMapping(path = "listed")
    public List<Product> getListed() {
        return this.productService.getListed();
    }

    @GetMapping(path = "all/{subCategoryId}")
    public ResponseEntity<?> getAllBySubCategory(@PathVariable Integer subCategoryId) {
        List<Product> lst = this.productService.getAllBySubCategory(subCategoryId);
        return ResponseEntity.ok(lst);
    }

    @GetMapping(path = "listed/{subCategoryId}")
    public ResponseEntity<?> getListedBySubCategory(@PathVariable Integer subCategoryId) {
        List<Product> lst = this.productService.getListedBySubCategory(subCategoryId);
        return ResponseEntity.ok(lst);
    }
}
