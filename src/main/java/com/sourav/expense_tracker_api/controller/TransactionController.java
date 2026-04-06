package com.sourav.expense_tracker_api.controller;

import com.sourav.expense_tracker_api.dto.*;
import com.sourav.expense_tracker_api.entity.Transaction;
import com.sourav.expense_tracker_api.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/user/{userId}/category/{categoryId}")
    public TransactionResponseDTO createTransaction(
            @PathVariable Long userId,
            @PathVariable Long categoryId,
            @Valid @RequestBody TransactionRequestDTO dto) {

        return transactionService.createTransaction(userId, categoryId, dto);
    }
    @GetMapping
    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
        return transactionService.getAllTransactions(pageable);
    }
    @GetMapping("/user/{userId}")
    public ApiResponse<?> getByUser(
            @PathVariable Long userId,
            Pageable pageable) {

        return ApiResponse.builder()
                .success(true)
                .message("Transactions fetched successfully")
                .data(transactionService.getTransactionsByUser(userId, pageable))
                .build();
    }
    @GetMapping("/category/{categoryId}")
    public List<TransactionResponseDTO> getByCategory(
            @PathVariable Long categoryId) {

        return transactionService.getByCategory(categoryId);
    }
    @GetMapping("/user/{userId}/category/{categoryId}")
    public List<TransactionResponseDTO> getByUserAndCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {

        return transactionService
                .getByUserAndCategory(userId, categoryId);
    }
    @GetMapping("user/{userId}/date-range")
    public List<TransactionResponseDTO> getByDateRange(
            @PathVariable Long userId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end
            ){
        return transactionService.getByUserAndDataRange(userId,start,end);
    }

    @GetMapping("/user/{userId}/total-expense")
    public ApiResponse<?> getTotalExpense(@PathVariable Long userId) {

        return ApiResponse.builder()
                .success(true)
                .message("Total expense fetched successfully")
                .data(transactionService.getTotalExpense(userId))
                .build();
    }
    @GetMapping("/user/{userId}/total-expense/date-range")
    public Double getTotalExpenseByDateRange(@PathVariable Long userId,
                                             @RequestParam LocalDate start,
                                             @RequestParam LocalDate end)
    {
        return transactionService.getTotalExpenseByDateRange(userId,start,end);
    }


    @GetMapping("/user/{userId}/category-summary")
    public ApiResponse<?> getCategorySummary(@PathVariable Long userId) {

        return ApiResponse.builder()
                .success(true)
                .message("Category summary fetched successfully")
                .data(transactionService.getCategorySummary(userId))
                .build();
    }
    @GetMapping("user/{userId}/top-category")
    public TopCategoryDTO getTopCategory(@PathVariable Long userId)
    {
        return  transactionService.getTopCategory(userId);
    }

}