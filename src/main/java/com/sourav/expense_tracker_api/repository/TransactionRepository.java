package com.sourav.expense_tracker_api.repository;

import com.sourav.expense_tracker_api.dto.CategorySummaryDTO;
import com.sourav.expense_tracker_api.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //List<Transaction> findByUserId(Long userId);

    List<Transaction> findByCategoryId(Long categoryId);
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);
    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate startDate,LocalDate endDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId")
    Double getTotalExpenseByUserId(Long userId);

    @Query("""
SELECT COALESCE(SUM(t.amount), 0)
FROM Transaction t
WHERE t.user.id = :userId
AND t.date BETWEEN :start AND :end
""")
    Double getTotalExpenseByUserIdAndDateRange(
            Long userId,
            LocalDate start,
            LocalDate end);

    @Query("""
SELECT new com.sourav.expense_tracker_api.dto.CategorySummaryDTO(
    t.category.id,
    SUM(t.amount)
)
FROM Transaction t
WHERE t.user.id = :userId
GROUP BY t.category.id
""")
    List<CategorySummaryDTO> getCategorySummary(Long userId);

}
