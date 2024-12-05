package com.hilwa.obligasi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hilwa.obligasi.models.Amortisasi;
import com.hilwa.obligasi.models.Journal;
import com.hilwa.obligasi.models.Transaction;
import com.hilwa.obligasi.repositories.AmortisasiRepository;
import com.hilwa.obligasi.repositories.JournalRepository;
import com.hilwa.obligasi.repositories.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    AmortisasiRepository amortisasiRepository;

    @Autowired
    JournalRepository journalRepository;

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public void deleteById(Integer id) {
        for (Amortisasi amor : amortisasiRepository.findAll()) {
            for (Journal journal : journalRepository.findAll()) {
                if (amor.getTransaction().getId() == id) {
                    amortisasiRepository.delete(amor);
                }
                if (journal.getTransaction().getId() == id){
                    journalRepository.delete(journal);
                }
            }
        }
        transactionRepository.deleteById(id);
    }

    public Transaction findById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> findByNama(String name){
        return transactionRepository.findAllByNamaPerusahaanContainingIgnoreCase(name);
    }

    public List<Transaction> findAllByOrderByDesc(){
        return transactionRepository.findAllByOrderByTanggalTerbitDesc();
    }

    public List<Transaction> findAllByOrderByAsalAsc(){
        return transactionRepository.findAllByOrderByTanggalTerbitAsc();
    }
}
