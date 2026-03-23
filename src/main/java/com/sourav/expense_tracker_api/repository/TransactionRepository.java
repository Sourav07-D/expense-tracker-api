package com.sourav.expense_tracker_api.repository;

import com.sourav.expense_tracker_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByCategoryId(Long categoryId);

    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
