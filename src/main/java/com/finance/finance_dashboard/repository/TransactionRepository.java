package com.finance.finance_dashboard.repository;

import com.finance.finance_dashboard.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDeletedFalse();

    List<Transaction> findByTypeAndDeletedFalse(String type);

    List<Transaction> findByCategoryAndDeletedFalse(String category);

    List<Transaction> findByDateBetweenAndDeletedFalse(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type AND t.deleted = false")
    BigDecimal sumAmountByType(String type);

    @Query("SELECT t.category, COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.deleted = false GROUP BY t.category")
    List<Object[]> sumAmountByCategory();

    @Query("SELECT t FROM Transaction t WHERE t.deleted = false ORDER BY t.date DESC LIMIT 5")
    List<Transaction> findRecentTransactions();
}