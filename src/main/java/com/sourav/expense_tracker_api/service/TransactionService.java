package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.dto.*;
import com.sourav.expense_tracker_api.entity.Category;
import com.sourav.expense_tracker_api.entity.Transaction;
import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.exception.ResourceNotFoundException;
import com.sourav.expense_tracker_api.mapper.TransactionMapper;
import com.sourav.expense_tracker_api.repository.CategoryRepository;
import com.sourav.expense_tracker_api.repository.TransactionRepository;
import com.sourav.expense_tracker_api.repository.UserRepository;
import com.sourav.expense_tracker_api.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    // ============================
    // CREATE TRANSACTION
    // ============================

    @Caching(evict = {
            // ✅ FIX: Proper cache key format (was inconsistent before)
            @CacheEvict(value = "totalExpense", key = "'user:' + #userId"),
            @CacheEvict(value = "categorySummary", key = "'user:' + #userId"),
            @CacheEvict(value = "totalExpenseByDate", allEntries = true) // multiple combinations
    })
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

    // ============================
    // READ OPERATIONS
    // ============================

    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
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

        Page<Long> idsPage = transactionRepository.findIdsByUserId(userId, pageable);

        List<Transaction> transactions =
                transactionRepository.findByIdsWithRelations(idsPage.getContent());

        return new PageImpl<>(
                transactions.stream().map(TransactionMapper::toDTO).toList(),
                pageable,
                idsPage.getTotalElements()
        );
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

    public List<TransactionResponseDTO> getByUserAndDataRange(
            Long userId,
            LocalDate start,
            LocalDate end) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Transaction> transactions =
                transactionRepository.findByUserIdAndDateBetween(userId, start, end);

        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    // ============================
    // CACHED METHODS (FIXED)
    // ============================

    @Cacheable(
            value = "totalExpense",
            key = "'user:' + #userId" // ✅ FIXED (was "#userId")
    )
    public double getTotalExpense(Long userId) {

        log.info("Fetching total expense for userId={} from DB", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return transactionRepository.getTotalExpenseByUserId(userId);
    }

    @Cacheable(
            value = "totalExpenseByDate",
            key = "'user:' + #userId + ':start:' + #start + ':end:' + #end" // ✅ FIXED KEY
    )
    public double getTotalExpenseByDateRange(
            Long userId,
            LocalDate start,
            LocalDate end) {

        log.info("Fetching date range expense from DB for userId={}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return transactionRepository
                .getTotalExpenseByUserIdAndDateRange(userId, start, end);
    }

    @Cacheable(
            value = "categorySummary",
            key = "'user:' + #userId" // ✅ FIXED
    )
    public List<CategorySummaryDTO> getCategorySummary(Long userId) {

        log.info("Fetching category summary from DB for userId={}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return transactionRepository.getCategorySummary(userId);
    }

    public TopCategoryDTO getTopCategory(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<TopCategoryDTO> list =
                transactionRepository.findTopCategory(userId);

        return list.isEmpty() ? null : list.get(0);
    }

    // ============================
    // UPDATE TRANSACTION
    // ============================

    @Caching(evict = {
            @CacheEvict(value = "totalExpense", key = "'user:' + #userId"),
            @CacheEvict(value = "categorySummary", key = "'user:' + #userId"),
            @CacheEvict(value = "totalExpenseByDate", allEntries = true)
    })
    public TransactionResponseDTO updateTransaction(
            Long userId,
            Long transactionId,
            TransactionRequestDTO dto) {

        log.info("Updating transactionId={} for userId={}", transactionId, userId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    log.error("Transaction not found id={}", transactionId);
                    return new ResourceNotFoundException("Transaction not found");
                });

        // ✅ SECURITY CHECK
        if (!transaction.getUser().getId().equals(userId)) {
            log.error("Unauthorized update attempt userId={}", userId);
            throw new RuntimeException("Unauthorized");
        }

        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());
        transaction.setDate(dto.getDate());

        Transaction updated = transactionRepository.save(transaction);

        log.info("Transaction updated successfully id={}", updated.getId());

        return TransactionMapper.toDTO(updated);
    }

    // ============================
    // DELETE TRANSACTION
    // ============================

    @Caching(evict = {
            @CacheEvict(value = "totalExpense", key = "'user:' + #userId"),
            @CacheEvict(value = "categorySummary", key = "'user:' + #userId"),
            @CacheEvict(value = "totalExpenseByDate", allEntries = true)
    })
    public void deleteTransaction(Long userId, Long transactionId) {

        log.info("Deleting transactionId={} for userId={}", transactionId, userId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    log.error("Transaction not found id={}", transactionId);
                    return new ResourceNotFoundException("Transaction not found");
                });

        // ✅ SECURITY CHECK
        if (!transaction.getUser().getId().equals(userId)) {
            log.error("Unauthorized delete attempt userId={}", userId);
            throw new RuntimeException("Unauthorized");
        }

        transactionRepository.delete(transaction);

        log.info("Transaction deleted successfully id={}", transactionId);
    }

    public Page<TransactionResponseDTO> filterTransactions(
            TransactionFilterDTO filter,
            Pageable pageable) {

        log.info("Filtering transactions with dynamic filters");

        Specification<Transaction> spec =
                TransactionSpecification.filter(filter);

        Page<Transaction> page =
                transactionRepository.findAll(spec, pageable);

        return page.map(TransactionMapper::toDTO);
    }
}