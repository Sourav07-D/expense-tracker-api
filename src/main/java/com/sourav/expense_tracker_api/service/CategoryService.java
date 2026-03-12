package com.sourav.expense_tracker_api.service;

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

    public Category createCategory(Long userId,Category category)
    {
        User user=userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        category.setUser(user);
        category.setCreatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }
public List<Category> getAllCategories()
{
    return categoryRepository.findAll();
}
}
