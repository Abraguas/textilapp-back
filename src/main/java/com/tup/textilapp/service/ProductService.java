package com.tup.textilapp.service;

import com.tup.textilapp.model.entity.*;
import com.tup.textilapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final UnitRepository unitRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ColorRepository colorRepository;

    @Autowired
    public ProductService(
            ProductRepository productRepository,
            BrandRepository brandRepository,
            UnitRepository unitRepository,
            SubCategoryRepository subCategoryRepository,
            ColorRepository colorRepository
    ) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.unitRepository = unitRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.colorRepository = colorRepository;
    }

    public void save(Product product) throws IllegalArgumentException {
        Optional<Brand> brand = this.brandRepository.findById(product.getBrand().getId());
        if (brand.isEmpty()) {
            throw new IllegalArgumentException("Specified brand doesn't exist");
        }
        Optional<Color> color = this.colorRepository.findById(product.getColor().getId());
        if (color.isEmpty()) {
            throw new IllegalArgumentException("Specified brand doesn't exist");
        }
        Optional<Unit> unit = this.unitRepository.findById(product.getUnit().getId());
        if (unit.isEmpty()) {
            throw new IllegalArgumentException("Specified unit doesn't exist");
        }
        Optional<SubCategory> subCategory = this.subCategoryRepository.findById(product.getSubCategory().getId());
        if (subCategory.isEmpty()) {
            throw new IllegalArgumentException("Specified subCategory doesn't exist");
        }
        Product newProduct = new Product(
                null,
                product.getName(),
                product.getDescription(),
                product.getPricePerUnit(),
                product.getObservations(),
                product.getStock(),
                product.getMeasurment(),
                product.getImage(),
                brand.get(),
                unit.get(),
                color.get(),
                subCategory.get()
        );
        this.productRepository.save(newProduct);
    }
    public List<Product> getAll() {
        return this.productRepository.findAll();
    }
    public List<Product> getAllBySubCategory(Integer id) {
        Optional<SubCategory> subCategory = this.subCategoryRepository.findById(id);
        if (subCategory.isEmpty()) {
            throw new IllegalArgumentException("Specified subCategory doesn't exist");
        }
        return this.productRepository.findAllBySubCategory(subCategory.get());
    }
}
