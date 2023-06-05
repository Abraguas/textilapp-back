package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.model.entity.Brand;
import com.tup.textilapp.service.BrandService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "brand")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<Brand> getAll() {
        return this.brandService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Brand brand) {

        this.brandService.save(brand);
        return ResponseEntity.ok(new ResponseMessageDTO("Brand registered succesfully"));

    }

    @PutMapping(path = "{brandId}")
    public ResponseEntity<?> update(@RequestBody Brand brand, @PathVariable Integer brandId) {

        this.brandService.update(brand, brandId);
        return ResponseEntity.ok(new ResponseMessageDTO("Brand updated succesfully"));

    }

    @DeleteMapping(path = "{brandId}")
    public ResponseEntity<?> delete(@PathVariable Integer brandId) {
        this.brandService.delete(brandId);
        return ResponseEntity.ok(new ResponseMessageDTO("Brand deleted succesfully"));
    }
}