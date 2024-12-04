package com.hilwa.obligasi.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hilwa.obligasi.models.Amortisasi;
import com.hilwa.obligasi.models.Journal;
import com.hilwa.obligasi.models.Transaction;
import com.hilwa.obligasi.repositories.AmortisasiRepository;

@Service
public class AmortisasiService {
    @Autowired
    AmortisasiRepository amortisasiRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    JournalService journalService;

    public List<Amortisasi> getAllAmortisasi() {
        return amortisasiRepository.findAll();
    }

    public Amortisasi findAmortisasiById(Integer id) {
        Transaction transaction = transactionService.findById(id);
        Journal journal = journalService.findJournalById(id);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }

        Amortisasi amortisasi = new Amortisasi();
        amortisasi.setTransaction(transaction);

        Amortisasi amortisasion = new Amortisasi();
        amortisasion.setJournal(journal);
        if (transaction.getKurs() > 100) {
            LocalDate tanggalBayar = transaction.getTanggalTerbit().toLocalDate();
            LocalDate tanggalBayarBunga = tanggalBayar.plus(1, ChronoUnit.YEARS);
            amortisasi.setTanggalBayarBunga(java.sql.Date.valueOf(tanggalBayarBunga));

            LocalDate tanggalBayarr = transaction.getTanggalTerbit().toLocalDate();
            LocalDate tanggalBayarrBunga = tanggalBayarr.plus(1, ChronoUnit.YEARS);
            amortisasi.setTanggalBayarrBunga(java.sql.Date.valueOf(tanggalBayarrBunga));

            Double bungaDibayarkan = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            amortisasi.setBungaDibayarkan(bungaDibayarkan);

            Double yangBelumAmortisasi = amortisasi.getAmortisasiDiskonto() - journal.getKPenerbitanPremi();
            amortisasi.getYangBelumAmortisasi();

            Double nilaiBukuObligasi = journal.getKPenerbitanUtangObligasi() + amortisasi.getYangBelumAmortisasi();
            amortisasi.setNilaiBukuObligasi(nilaiBukuObligasi);

            Double bebanBunga = (transaction.getSukuBungaPasar() / 100) * amortisasi.getNilaiBukuObligasi();
            amortisasi.setBebanBunga(bebanBunga);
        }else{
            
        }
    }
}
