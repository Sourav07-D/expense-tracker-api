package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.dto.TransactionRequestDTO;
import com.sourav.expense_tracker_api.dto.TransactionResponseDTO;
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

    public TransactionResponseDTO createTransaction(
            Long userId,
            Long categoryId,
            TransactionRequestDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = Transaction.builder()
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .date(dto.getDate())
                .user(user)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(transaction);

        return TransactionResponseDTO.builder()
                .id(saved.getId())
                .amount(saved.getAmount())
                .description(saved.getDescription())
                .date(saved.getDate())
                .userId(userId)
                .categoryId(categoryId)
                .build();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    public List<TransactionResponseDTO> getTransactionsByUser(Long userId) {

        List<Transaction> transactions =
                transactionRepository.findByUserId(userId);

        return transactions.stream()
                .map(this::mapToDTO)
                .toList();
    }
    public List<TransactionResponseDTO> getByCategory(Long categoryId) {

        List<Transaction> transactions =
                transactionRepository.findByCategoryId(categoryId);

        return transactions.stream()
                .map(this::mapToDTO)
                .toList();
    }
    public List<TransactionResponseDTO> getByUserAndCategory(
            Long userId,
            Long categoryId) {

        List<Transaction> transactions =
                transactionRepository
                        .findByUserIdAndCategoryId(userId, categoryId);

        return transactions.stream()
                .map(this::mapToDTO)
                .toList();
    }
    private TransactionResponseDTO mapToDTO(Transaction t) {
        return TransactionResponseDTO.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .description(t.getDescription())
                .date(t.getDate())
                .userId(t.getUser().getId())
                .categoryId(t.getCategory().getId())
                .build();
    }
}