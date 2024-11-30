package com.hilwa.obligasi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hilwa.obligasi.models.Journal;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
}
