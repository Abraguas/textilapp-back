package com.tup.textilapp.service;

import com.tup.textilapp.model.dto.CategoryDTO;
import com.tup.textilapp.model.dto.SubCategoryDTO;
import com.tup.textilapp.model.entity.SubCategory;
import com.tup.textilapp.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final SubCategoryRepository subCategoryRepository;

    @Autowired
    public CategoryService(
            SubCategoryRepository subCategoryRepository
    ) {
        this.subCategoryRepository = subCategoryRepository;
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
}
