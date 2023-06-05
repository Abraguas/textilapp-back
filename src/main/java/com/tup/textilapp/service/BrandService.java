package com.tup.textilapp.service;

import com.tup.textilapp.model.entity.Brand;
import com.tup.textilapp.repository.BrandRepository;
import com.tup.textilapp.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    public BrandService(BrandRepository brandRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
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
                .orElseThrow(() -> new EntityNotFoundException("Specified brand doesn't exist"));
        if (newBrand.getName() != null
                && !newBrand.getName().equals(brand.getName())
                && newBrand.getName().length() > 0) {
            brand.setName(newBrand.getName());
        }
    }
    public void delete(Integer brandId) {
        this.brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Brand with specified id doesn't exist"));
        if(this.productRepository.existsAllByBrand_Id(brandId)) {
            throw new IllegalStateException("Cannot delete a brand with existing products");
        }
        this.brandRepository.deleteById(brandId);
    }

}
