package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.dto.CategoryRequestDTO;
import com.sourav.expense_tracker_api.dto.CategoryResponseDTO;
import com.sourav.expense_tracker_api.entity.Category;
import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.repository.CategoryRepository;
import com.sourav.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;



    public CategoryResponseDTO createCategory(Long userId,
                                              CategoryRequestDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = Category.builder()
                .name(dto.getName())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        Category saved = categoryRepository.save(category);

        return CategoryResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .userId(userId)
                .build();
    }
public List<Category> getAllCategories()
{
    return categoryRepository.findAll();
}
}
