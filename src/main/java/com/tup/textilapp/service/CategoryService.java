package com.tup.textilapp.service;

import com.tup.textilapp.model.dto.CategoryDTO;
import com.tup.textilapp.model.dto.SubCategoryDTO;
import com.tup.textilapp.model.entity.Category;
import com.tup.textilapp.model.entity.SubCategory;
import com.tup.textilapp.repository.CategoryRepository;
import com.tup.textilapp.repository.ProductRepository;
import com.tup.textilapp.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(
            SubCategoryRepository subCategoryRepository,
            CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryDTO> mapSubCategoriesToCategoryDTOs(List<SubCategory> subCategories) {
        Map<Integer, CategoryDTO> categoriesMap = new HashMap<>();

        for (SubCategory subCategory : subCategories) {
            Integer categoryId = subCategory.getCategory().getId();
            CategoryDTO categoryDTO = categoriesMap.get(categoryId);

            if (categoryDTO == null) {
                categoryDTO = new CategoryDTO(categoryId, subCategory.getCategory().getName(), new ArrayList<>());
                categoriesMap.put(categoryId, categoryDTO);
            }

            categoryDTO.getSubCategories().add(new SubCategoryDTO(subCategory.getId(), subCategory.getName()));
        }

        return new ArrayList<>(categoriesMap.values());
    }

    public List<CategoryDTO> getAll() {
        return mapSubCategoriesToCategoryDTOs(this.subCategoryRepository.findAll());
    }
    public List<Category> getCategories() {
        return this.categoryRepository.findAll();
    }
    public List<SubCategory> getSubCategories() {
        return this.subCategoryRepository.findAll();
    }
    public void saveCategory(Category category) {
        this.categoryRepository.save(category);
    }
    public void saveSubcategory(SubCategory subCategory) {
        Category category = this.categoryRepository.findById(subCategory.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category doesn't exist"));
        subCategory.setCategory(category);
        this.subCategoryRepository.save(subCategory);
    }
    public void deleteSubcategory(Integer subCategoryId) {
        this.subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory with specified id doesn't exist"));
        if(this.productRepository.existsAllBySubCategory_Id(subCategoryId)) {
            throw new IllegalStateException("There's already products in this subcategory");
        }
        this.subCategoryRepository.deleteById(subCategoryId);
    }
    public void deleteCategory(Integer categoryId) {
        this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with specified id doesn't exist"));
        if(this.subCategoryRepository.existsAllByCategory_Id(categoryId)) {
            throw new IllegalStateException("Cannot delete a category that contains subcategories");
        }
        this.categoryRepository.deleteById(categoryId);
    }
    @Transactional
    public void updateCategory(Category newCat, Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with specified id doesn't exist"));
        if(newCat.getName() != null
                && !newCat.getName().equals(category.getName())
                && newCat.getName().length() > 0) {
            category.setName(newCat.getName());
        }
    }
    @Transactional
    public void updateSubCategory(SubCategory newSubCat, Integer subCategoryId) {
        SubCategory subCategory = this.subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory with specified id doesn't exist"));
        if(newSubCat.getName() != null
                && !newSubCat.getName().equals(subCategory.getName())
                && newSubCat.getName().length() > 0) {
            subCategory.setName(newSubCat.getName());
        }
    }
}
