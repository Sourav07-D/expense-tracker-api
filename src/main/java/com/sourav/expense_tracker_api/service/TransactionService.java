package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.dto.CategorySummaryDTO;
import com.sourav.expense_tracker_api.dto.TopCategoryDTO;
import com.sourav.expense_tracker_api.dto.TransactionRequestDTO;
import com.sourav.expense_tracker_api.dto.TransactionResponseDTO;
import com.sourav.expense_tracker_api.entity.Category;
import com.sourav.expense_tracker_api.entity.Transaction;
import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.exception.ResourceNotFoundException;
import com.sourav.expense_tracker_api.mapper.TransactionMapper;
import com.sourav.expense_tracker_api.repository.CategoryRepository;
import com.sourav.expense_tracker_api.repository.TransactionRepository;
import com.sourav.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
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
    private static final Logger log =
            LoggerFactory.getLogger(TransactionService.class);

    public TransactionResponseDTO createTransaction(
            Long userId,
            Long categoryId,
            TransactionRequestDTO dto) {

        log.info("Creating transaction for userId={} categoryId={}", userId, categoryId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with id={}", categoryId);
                    return new ResourceNotFoundException("Category not found");
                });

        Transaction transaction = Transaction.builder()
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .date(dto.getDate())
                .user(user)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(transaction);

        log.info("Transaction created successfully with id={}", saved.getId());

        return TransactionMapper.toDTO(saved);
    }


    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
     Page<Transaction> transactions=transactionRepository.findAll(pageable);
     return transactions.map(TransactionMapper::toDTO);
    }

    public Page<TransactionResponseDTO> getTransactionsByUser(
            Long userId,
            Pageable pageable) {

        log.info("Fetching transactions for userId={} page={} size={}",
                userId, pageable.getPageNumber(), pageable.getPageSize());

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        Page<Transaction> transactions =
                transactionRepository.findByUserId(userId, pageable);

        log.info("Fetched {} transactions", transactions.getNumberOfElements());

        return transactions.map(TransactionMapper::toDTO);
    }
    public List<TransactionResponseDTO> getByCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        List<Transaction> transactions =
                transactionRepository.findByCategoryId(categoryId);

        return transactions.stream()
                .map(TransactionMapper::toDTO)
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
                .map(TransactionMapper::toDTO)
                .toList();
    }
    public List<TransactionResponseDTO> getByUserAndDataRange(Long userId, LocalDate start,LocalDate end)
    {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Transaction> transactions=transactionRepository.findByUserIdAndDateBetween(userId,start
        ,end);
        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }
//    public double getTotalExpense(Long userId)
//    {
//        userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        return transactionRepository.getTotalExpenseByUserId(userId);
//    }
    @Cacheable(value = "totalExpense", key = "#userId")
    public double getTotalExpense(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        System.out.println("🔥 Fetching from DB...");

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
