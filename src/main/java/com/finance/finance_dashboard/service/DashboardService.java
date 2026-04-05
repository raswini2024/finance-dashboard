package com.finance.finance_dashboard.service;

import com.finance.finance_dashboard.dto.DashboardSummary;
import com.finance.finance_dashboard.model.Transaction;
import com.finance.finance_dashboard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardSummary getSummary() {
        BigDecimal totalIncome = transactionRepository.sumAmountByType("INCOME");
        BigDecimal totalExpense = transactionRepository.sumAmountByType("EXPENSE");
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        List<Object[]> categoryData = transactionRepository.sumAmountByCategory();
        Map<String, BigDecimal> categoryWiseTotals = new HashMap<>();
        for (Object[] row : categoryData) {
            String category = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];
            categoryWiseTotals.put(category, total);
        }

        long totalTransactions = transactionRepository.findByDeletedFalse().size();

        return DashboardSummary.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .categoryWiseTotals(categoryWiseTotals)
                .totalTransactions(totalTransactions)
                .build();
    }

    public List<Transaction> getRecentTransactions() {
        return transactionRepository.findRecentTransactions();
    }
}