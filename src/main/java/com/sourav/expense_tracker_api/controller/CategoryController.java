package com.sourav.expense_tracker_api.controller;

import com.sourav.expense_tracker_api.entity.Category;
import com.sourav.expense_tracker_api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/user/{userId}")
    public Category createCategory(
            @PathVariable Long userId,
            @RequestBody Category category) {

        return categoryService.createCategory(userId, category);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}