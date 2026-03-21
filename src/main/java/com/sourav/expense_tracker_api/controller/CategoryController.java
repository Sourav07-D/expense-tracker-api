package com.sourav.expense_tracker_api.controller;

import com.sourav.expense_tracker_api.dto.CategoryRequestDTO;
import com.sourav.expense_tracker_api.dto.CategoryResponseDTO;
import com.sourav.expense_tracker_api.entity.Category;
import com.sourav.expense_tracker_api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/user/{userId}")
    public CategoryResponseDTO createCategory(
            @PathVariable Long userId,
            @Valid @RequestBody CategoryRequestDTO dto) {

        return categoryService.createCategory(userId, dto);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}