package com.hilwa.obligasi.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hilwa.obligasi.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByNamaPerusahaanContainingIgnoreCase(String name);
    List<Transaction> findAllByOrderByTanggalTerbitDesc();
    List<Transaction> findAllByOrderByTanggalTerbitAsc();
}
