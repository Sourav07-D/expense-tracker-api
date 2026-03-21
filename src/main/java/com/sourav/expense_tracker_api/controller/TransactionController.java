package com.sourav.expense_tracker_api.controller;

import com.sourav.expense_tracker_api.dto.TransactionRequestDTO;
import com.sourav.expense_tracker_api.dto.TransactionResponseDTO;
import com.sourav.expense_tracker_api.entity.Transaction;
import com.sourav.expense_tracker_api.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}