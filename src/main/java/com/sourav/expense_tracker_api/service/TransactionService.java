package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.dto.CategorySummaryDTO;
import com.sourav.expense_tracker_api.dto.TopCategoryDTO;
import com.sourav.expense_tracker_api.dto.TransactionRequestDTO;
import com.sourav.expense_tracker_api.dto.TransactionResponseDTO;
import com.sourav.expense_tracker_api.entity.Category;
import com.sourav.expense_tracker_api.entity.Transaction;
import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.exception.ResourceNotFoundException;
import com.sourav.expense_tracker_api.repository.CategoryRepository;
import com.sourav.expense_tracker_api.repository.TransactionRepository;
import com.sourav.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.ToLongFunction;

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

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
    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
     Page<Transaction> transactions=transactionRepository.findAll(pageable);
     return transactions.map(this::mapToDTO);
    }
    public Page<TransactionResponseDTO> getTransactionsByUser(Long userId,Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Page<Transaction> transactions =
                transactionRepository.findByUserId(userId,pageable);

        return transactions.map(this::mapToDTO);
    }
    public List<TransactionResponseDTO> getByCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        List<Transaction> transactions =
                transactionRepository.findByCategoryId(categoryId);

        return transactions.stream()
                .map(this::mapToDTO)
                .toList();
    }
    public List<TransactionResponseDTO> getByUserAndCategory(
            Long userId,
            Long categoryId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));


        List<Transaction> transactions =
                transactionRepository
                        .findByUserIdAndCategoryId(userId, categoryId);

        return transactions.stream()
                .map(this::mapToDTO)
                .toList();
    }
    public List<TransactionResponseDTO> getByUserAndDataRange(Long userId, LocalDate start,LocalDate end)
    {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Transaction> transactions=transactionRepository.findByUserIdAndDateBetween(userId,start
        ,end);
        return transactions.stream()
                .map(this:: mapToDTO)
                .toList();
    }
    public double getTotalExpense(Long userId)
    {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return transactionRepository.getTotalExpenseByUserId(userId);
    }
    public double getTotalExpenseByDateRange(Long userId,LocalDate start,LocalDate end)
    {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return transactionRepository.getTotalExpenseByUserIdAndDateRange(userId,start,end);
    }
    public List<CategorySummaryDTO> getCategorySummary(Long userId)
    {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return transactionRepository.getCategorySummary(userId);
    }
    public TopCategoryDTO getTopCategory(Long userId)
    {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
         List<TopCategoryDTO> list=transactionRepository.findTopCategory(userId);
         if(list.isEmpty())
         {
             return null;
         }
         return list.get(0);
    }


}
