package com.elearning.backend.controller;

import com.elearning.backend.model.Category;
import com.elearning.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    ///
    /// Constructor for CategoryController
    /// params: CategoryService categoryService
    /// return: None
    /// description: This constructor initializes the CategoryController with the provided CategoryService.
    @PostMapping
    public String createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return "Category created successfully";
    }

    ///
    /// Get category by id
    /// params: Long id
    /// return: Category
    /// description: This method retrieves a category by its ID.
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryService.getCategoryById(id).orElse(null);
    }
}
