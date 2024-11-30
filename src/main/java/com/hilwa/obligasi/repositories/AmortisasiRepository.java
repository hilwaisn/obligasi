package com.hilwa.obligasi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hilwa.obligasi.models.Amortisasi;

public interface AmortisasiRepository extends JpaRepository<Amortisasi, Integer> {
}
