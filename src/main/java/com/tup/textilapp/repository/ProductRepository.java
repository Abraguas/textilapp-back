package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.Product;
import com.tup.textilapp.model.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllBySubCategory(SubCategory subCategory);
    boolean existsAllBySubCategory_Id(Integer subCategoryId);
    boolean existsAllByBrand_Id(Integer brandId);
    List<Product> findAllByIsListed(Boolean isListed);
    List<Product> findAllByIsListedAndSubCategory(Boolean isListed, SubCategory subCategory);
    List<Product> findAllByNameContainingIgnoreCase(String infix);
}
