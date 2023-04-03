package com.tup.textilapp.config;

import com.tup.textilapp.model.entity.Category;
import com.tup.textilapp.model.entity.SubCategory;
import com.tup.textilapp.repository.CategoryRepository;
import com.tup.textilapp.repository.SubCategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class CategoryConfig implements CommandLineRunner {

    private final CategoryRepository catRepository;
    private final SubCategoryRepository subCatRepository;

    public CategoryConfig(
            CategoryRepository catRepository,
            SubCategoryRepository subCatRepository
    ) {
        this.catRepository = catRepository;
        this.subCatRepository = subCatRepository;
    }

    @Transactional
    @Override
    public void run(String... args) {
        List<Category> list = new ArrayList<>();
        list.add(new Category(null, "Cama"));
        list.add(new Category(null, "Baños"));
        list.add(new Category(null, "Cocina"));
        list.add(new Category(null, "Deco"));
        list.add(new Category(null, "Telas"));
        catRepository.saveAll(list);

        List<SubCategory> subList = new ArrayList<>();
        Category bed = catRepository.findByName("Cama");
        subList.add(new SubCategory(null,"Almohadas", bed));
        subList.add(new SubCategory(null,"Sabanas", bed));
        subList.add(new SubCategory(null,"Fundas", bed));

        Category bath = catRepository.findByName("Baños");
        subList.add(new SubCategory(null,"Cortinas de baño", bath));
        subList.add(new SubCategory(null,"Alfombras de baño", bath));
        subList.add(new SubCategory(null,"Toallones", bath));

        Category kit = catRepository.findByName("Cocina");
        subList.add(new SubCategory(null,"Repasadores", kit));
        subList.add(new SubCategory(null,"Sabanas", kit));
        subList.add(new SubCategory(null,"Fundas", kit));

        Category dec = catRepository.findByName("Deco");
        subList.add(new SubCategory(null,"Alfombras", dec));
        subList.add(new SubCategory(null,"Cortinas", dec));
        subList.add(new SubCategory(null,"Almohadones", dec));

        Category clo = catRepository.findByName("Telas");
        subList.add(new SubCategory(null,"Friselina", clo));
        subList.add(new SubCategory(null,"Jean", clo));
        subList.add(new SubCategory(null,"Corderito", clo));
        subCatRepository.saveAll(subList);


    }
}
