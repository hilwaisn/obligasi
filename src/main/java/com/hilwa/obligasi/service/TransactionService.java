package com.hilwa.obligasi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hilwa.obligasi.models.Transaction;
import com.hilwa.obligasi.repositories.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public void deleteById(Integer id) {
        transactionRepository.deleteById(id);
    }

    public Transaction findById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }
}
