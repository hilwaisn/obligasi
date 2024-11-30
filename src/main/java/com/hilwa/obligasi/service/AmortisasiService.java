package com.hilwa.obligasi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hilwa.obligasi.models.Amortisasi;
import com.hilwa.obligasi.repositories.AmortisasiRepository;

@Service
public class AmortisasiService {
    @Autowired
    AmortisasiRepository amortisasiRepository;

    public List<Amortisasi> getAllAmortisasi() {
        return amortisasiRepository.findAll();
    }
}
