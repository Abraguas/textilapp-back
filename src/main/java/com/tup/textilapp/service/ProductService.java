package com.tup.textilapp.service;

import com.tup.textilapp.model.entity.*;
import com.tup.textilapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
        Brand brand = this.brandRepository.findById(product.getBrand().getId())
                .orElseThrow(() -> new IllegalArgumentException("Specified brand doesn't exist"));

        Color color = this.colorRepository.findById(product.getColor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Specified color doesn't exist"));

        Unit unit = this.unitRepository.findById(product.getUnit().getId())
                .orElseThrow(() -> new IllegalArgumentException("Specified unit doesn't exist"));

        SubCategory subCategory = this.subCategoryRepository.findById(product.getSubCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Specified subCategory doesn't exist"));

        Product newProduct = new Product(
                null,
                product.getName(),
                product.getDescription(),
                product.getPricePerUnit(),
                product.getObservations(),
                0,
                product.getMeasurment(),
                product.getImage(),
                product.getIsListed(),
                brand,
                unit,
                color,
                subCategory
        );
        this.productRepository.save(newProduct);
    }

    @Transactional
    public void update(Integer id, Product newProduct) throws IllegalArgumentException {

        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specified product doesn't exist"));

        if (newProduct.getBrand() != null
                && !Objects.equals(newProduct.getBrand().getId(), product.getBrand().getId())) {
            Brand brand = this.brandRepository.findById(newProduct.getBrand().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Specified brand doesn't exist"));
            product.setBrand(brand);
        }
        if (newProduct.getColor() != null
                && !Objects.equals(newProduct.getColor().getId(), product.getColor().getId())) {
            Color color = this.colorRepository.findById(newProduct.getColor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Specified color doesn't exist"));
            product.setColor(color);
        }
        if (newProduct.getUnit() != null
                && !Objects.equals(newProduct.getUnit().getId(), product.getUnit().getId())) {
            Unit unit = this.unitRepository.findById(newProduct.getUnit().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Specified unit doesn't exist"));
            product.setUnit(unit);
        }
        if (newProduct.getSubCategory() != null
                && !Objects.equals(newProduct.getSubCategory().getId(), product.getSubCategory().getId())) {
            SubCategory subCategory = this.subCategoryRepository.findById(newProduct.getSubCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Specified subCategory doesn't exist"));
            product.setSubCategory(subCategory);
        }
        if (newProduct.getName() != null
                && !newProduct.getName().equals(product.getName())
                && newProduct.getName().length() > 0) {
            product.setName(newProduct.getName());
        }
        if (newProduct.getDescription() != null
                && !newProduct.getDescription().equals(product.getDescription())
                && newProduct.getDescription().length() > 0) {
            product.setDescription(newProduct.getDescription());
        }
        if (newProduct.getPricePerUnit() != null
                && !Objects.equals(newProduct.getPricePerUnit(), product.getPricePerUnit())
                && newProduct.getPricePerUnit().compareTo(new BigDecimal(0)) != 0) {
            product.setPricePerUnit(newProduct.getPricePerUnit());
        }
        if (newProduct.getObservations() != null
                && !newProduct.getObservations().equals(product.getObservations())
                && newProduct.getObservations().length() > 0) {
            product.setObservations(newProduct.getObservations());
        }
        if (newProduct.getStock() != null
                && !Objects.equals(newProduct.getStock(), product.getStock())
                && newProduct.getStock() > 0) {
            product.setStock(newProduct.getStock());
        }
        if (newProduct.getMeasurment() != null
                && !newProduct.getMeasurment().equals(product.getMeasurment())
                && newProduct.getMeasurment().length() > 0) {
            product.setMeasurment(newProduct.getMeasurment());
        }
        if (newProduct.getImage() != null
                && !newProduct.getImage().equals(product.getImage())
                && newProduct.getImage().length() > 0) {
            product.setImage(newProduct.getImage());
        }
        if (newProduct.getIsListed() != null
                && !newProduct.getIsListed().equals(product.getIsListed())) {
            product.setIsListed(newProduct.getIsListed());
        }

    }

    @Transactional
    public void unlist(Integer id) throws IllegalArgumentException {

        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specified product doesn't exist"));
        product.setIsListed(false);

    }
    @Transactional
    public void list(Integer id) throws IllegalArgumentException {

        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specified product doesn't exist"));
        product.setIsListed(true);

    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }
    public Product getById(Integer productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("Product with id " + productId + " doesn't exist"));
    }
    public List<Product> getListed() {
        return this.productRepository.findAllByIsListed(true);
    }
    public List<Product> getListedBySubCategory(Integer id) {
        SubCategory subCategory = this.subCategoryRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Specified subCategory doesn't exist"));

        return this.productRepository.findAllByIsListedAndSubCategory(true,subCategory);
    }
    public List<Product> getAllBySubCategory(Integer id) {
        SubCategory subCategory = this.subCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specified subCategory doesn't exist"));

        return this.productRepository.findAllBySubCategory(subCategory);
    }
}
