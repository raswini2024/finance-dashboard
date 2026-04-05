package com.finance.finance_dashboard.service;

import com.finance.finance_dashboard.dto.TransactionRequest;
import com.finance.finance_dashboard.exception.ResourceNotFoundException;
import com.finance.finance_dashboard.model.Transaction;
import com.finance.finance_dashboard.model.User;
import com.finance.finance_dashboard.repository.TransactionRepository;
import com.finance.finance_dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Transaction createTransaction(TransactionRequest request) {
        User currentUser = getCurrentUser();

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType().toUpperCase())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(currentUser)
                .deleted(false)
                .build();

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findByDeletedFalse();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    public Transaction updateTransaction(Long id, TransactionRequest request) {
        Transaction transaction = getTransactionById(id);

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType().toUpperCase());
        transaction.setCategory(request.getCategory());
        transaction.setDate(request.getDate());
        transaction.setNotes(request.getNotes());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = getTransactionById(id);
        transaction.setDeleted(true);
        transactionRepository.save(transaction);
    }

    public List<Transaction> filterByType(String type) {
        return transactionRepository.findByTypeAndDeletedFalse(type.toUpperCase());
    }

    public List<Transaction> filterByCategory(String category) {
        return transactionRepository.findByCategoryAndDeletedFalse(category);
    }

    public List<Transaction> filterByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateBetweenAndDeletedFalse(startDate, endDate);
    }
}