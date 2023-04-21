package com.tup.textilapp.service;

import com.tup.textilapp.model.entity.Brand;
import com.tup.textilapp.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public void save(Brand brand) {
        System.out.println(brand.toString());
        if (brand.getName() != null
                && brand.getName().length() > 0) {
            this.brandRepository.save(brand);
        } else {
            throw new IllegalArgumentException("Invalid name provided");
        }
    }
    public List<Brand> getAll() {
        return this.brandRepository.findAll();
    }
    @Transactional
    public void update(Brand newBrand, Integer brandId) {
        Brand brand = this.brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("Specified brand doesn't exist"));
        if (newBrand.getName() != null
                && !newBrand.getName().equals(brand.getName())
                && newBrand.getName().length() > 0) {
            brand.setName(newBrand.getName());
        }
    }

}
