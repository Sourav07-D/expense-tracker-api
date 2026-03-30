package com.sourav.expense_tracker_api.repository;

import com.sourav.expense_tracker_api.entity.Category;
import com.sourav.expense_tracker_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByUserId(Long userId);
}
