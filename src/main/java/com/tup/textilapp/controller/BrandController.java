package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.Brand;
import com.tup.textilapp.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "brand")
public class BrandController {
    private final BrandService brandService;

    @Autowired
    public BrandController (BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<Brand> getAll() {
        return this.brandService.getAll();
    }
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Brand brand) {
        try {
            this.brandService.save(brand);
            return ResponseEntity.ok(new ResponseMessageDTO("Brand registered succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @PutMapping(path = "{brandId}")
    @Transactional
    public ResponseEntity<?> update(@RequestBody Brand brand, @PathVariable Integer brandId) {
        try {
            this.brandService.update(brand, brandId);
            return ResponseEntity.ok(new ResponseMessageDTO("Brand updated succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
}