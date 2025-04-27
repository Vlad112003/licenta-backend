package com.elearning.backend.service;

import com.elearning.backend.model.Category;
import com.elearning.backend.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Adaugarea unei categorii implicite
    @PostConstruct
    public void init() {
        if (categoryRepository.count() == 0) {
            Category defaultCategory = new Category();
            defaultCategory.setName("General");
            categoryRepository.save(defaultCategory);
        }
    }

    // Obține o categorie dupa id
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Creeaza o categorie noua
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Obține o categorie implicita
    public Category getDefaultCategory() {
        return categoryRepository.findById(1L).orElse(null);
    }
}
