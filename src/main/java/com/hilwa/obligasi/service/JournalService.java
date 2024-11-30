package com.hilwa.obligasi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hilwa.obligasi.models.Journal;
import com.hilwa.obligasi.repositories.JournalRepository;

@Service
public class JournalService {
    @Autowired
    JournalRepository journalRepository;

    public List<Journal> getAllJournal() {
        return journalRepository.findAll();
    }

    
}
