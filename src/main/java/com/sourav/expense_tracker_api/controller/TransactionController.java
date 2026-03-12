package com.sourav.expense_tracker_api.controller;

import com.sourav.expense_tracker_api.entity.Transaction;
import com.sourav.expense_tracker_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/user/{userId}/category/{categoryId}")
    public Transaction createTransaction(
            @PathVariable Long userId,
            @PathVariable Long categoryId,
            @RequestBody Transaction transaction) {

        return transactionService.createTransaction(userId, categoryId, transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}