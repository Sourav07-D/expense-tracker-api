package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.entity.Category;
import com.sourav.expense_tracker_api.entity.Transaction;
import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.repository.CategoryRepository;
import com.sourav.expense_tracker_api.repository.TransactionRepository;
import com.sourav.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public Transaction createTransaction(
            Long userId,
            Long categoryId,
            Transaction transaction) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}