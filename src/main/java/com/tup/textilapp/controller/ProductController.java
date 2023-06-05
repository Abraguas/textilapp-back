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
        try {
            this.productService.save(product);
            return ResponseEntity.ok(new ResponseMessageDTO("Product created succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PutMapping(path = "unlist/{productId}")
    public ResponseEntity<?> unlist(@PathVariable Integer productId) {
        try {
            this.productService.unlist(productId);
            return ResponseEntity.ok(new ResponseMessageDTO("Product unlisted succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PutMapping(path = "list/{productId}")
    public ResponseEntity<?> list(@PathVariable Integer productId) {
        try {
            this.productService.list(productId);
            return ResponseEntity.ok(new ResponseMessageDTO("Product listed succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @PutMapping(path = "{productId}")
    public ResponseEntity<?> update(@PathVariable Integer productId,@RequestBody Product product) {
        try {
            this.productService.update(productId ,product);
            return ResponseEntity.ok(new ResponseMessageDTO("Product updated succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping(path = "{productId}")
    public ResponseEntity<?> getById(@PathVariable Integer productId) {
        try {
            return ResponseEntity.ok(this.productService.getById(productId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }

    }
    @GetMapping(path = "all")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String searchString
    ) {
        try {
            if (searchString == null || searchString.length() < 1) {
                return ResponseEntity.ok(this.productService.getAll());
            }
            return ResponseEntity.ok(productService.searchByName(searchString));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping(path = "listed")
    public List<Product> getListed() {
        return this.productService.getListed();
    }
    @GetMapping(path = "all/{subCategoryId}")
    public ResponseEntity<?> getAllBySubCategory(@PathVariable Integer subCategoryId) {
        try {
            List<Product> lst = this.productService.getAllBySubCategory(subCategoryId);
            return ResponseEntity.ok(lst);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping(path = "listed/{subCategoryId}")
    public ResponseEntity<?> getListedBySubCategory(@PathVariable Integer subCategoryId) {
        try {
            List<Product> lst = this.productService.getListedBySubCategory(subCategoryId);
            return ResponseEntity.ok(lst);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
}
