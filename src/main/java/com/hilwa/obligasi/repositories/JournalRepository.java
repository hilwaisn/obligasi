package com.hilwa.obligasi.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hilwa.obligasi.models.Journal;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
    List<Journal> findByTransactionId(Integer transactionId);
}
