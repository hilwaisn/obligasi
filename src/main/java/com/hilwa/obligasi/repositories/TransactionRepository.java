package com.hilwa.obligasi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hilwa.obligasi.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
