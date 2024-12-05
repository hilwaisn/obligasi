package com.hilwa.obligasi.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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

    public String allAmortisasi(Model model) {
        List<Transaction> transactions = transactionService.getAllTransaction();
        List<Journal> journals = journalService.getAllJournal();
        List<Transaction> amortisasis = new ArrayList<>();
        List<Amortisasi> amortisasis2 = amortisasiRepository.findAll();
        boolean ada = false;
        if (transactions == null) {
            throw new IllegalArgumentException("Transaction not found");
        }
        if (journals == null) {
            throw new IllegalArgumentException("Journal not found");
        }

        for (Transaction transaction : transactions) {
            for (Amortisasi amortisasi : amortisasis2) {
                if (amortisasi.getTransaction().equals(transaction))
                    ada = true;
            }
            Amortisasi amortisasi = new Amortisasi();
            for (Journal journal : journals) {
                amortisasi.setTransaction(transaction);
                amortisasi.setJournal(journal);

                Double bungaDibayarkan = journal.getKBayarBungaAwal();
                amortisasi.setBungaDibayarkan(bungaDibayarkan);

                if (transaction.getKurs() > 100) {
                    Double yangBelumAmortisasi = journal.getKPenerbitanPremi();
                    amortisasi.setYangBelumAmortisasi(yangBelumAmortisasi);
                    Double nilaiBukuObligasi = journal.getDPenerbitanKas();
                    amortisasi.setNilaiBukuObligasi(nilaiBukuObligasi);
                } else {
                    Double yangBelumAmortisasi = journal.getDPenerbitanDiskon();
                    amortisasi.setYangBelumAmortisasi(yangBelumAmortisasi);
                    Double nilaiBukuObligasi = journal.getDPenerbitanDiskon();
                    amortisasi.setNilaiBukuObligasi(nilaiBukuObligasi);
                }

                if (!ada)
                    amortisasiRepository.save(amortisasi);

                List<Amortisasi> tableAmortisasis = new ArrayList<>();
                for (int i = 0; i < transaction.getJangkaWaktu(); i++) {
                    Amortisasi tableAmortisasi = new Amortisasi();
                    
                    LocalDate tanggalBayar = tableAmortisasis.isEmpty() ? transaction.getTanggalBayar().toLocalDate()
                            : tableAmortisasis.get(i - 1).getTanggalBayarBunga().toLocalDate();
                    LocalDate tanggalBayarBunga = tableAmortisasis.isEmpty() ? tanggalBayar
                            : tanggalBayar.plus(1, ChronoUnit.YEARS);
                    tableAmortisasi.setTanggalBayarBunga(Date.valueOf(tanggalBayarBunga));
    
                    LocalDate tanggalBayarr = tableAmortisasis.isEmpty() ? transaction.getTanggalBayarr().toLocalDate()
                            : tableAmortisasis.get(i - 1).getTanggalBayarrBunga().toLocalDate();
                    LocalDate tanggalBayarrBunga = tableAmortisasis.isEmpty() ? tanggalBayarr
                            : tanggalBayarr.plus(1, ChronoUnit.YEARS);
                    tableAmortisasi.setTanggalBayarrBunga(Date.valueOf(tanggalBayarrBunga));
                    tableAmortisasi.setBungaDibayarkan(amortisasi.getBungaDibayarkan());

                    
                    if (amortisasi.getYangBelumAmortisasi() != null && amortisasi.getNilaiBukuObligasi() != null) {

                        // Beban Bunga
                        Double bebanBunga = tableAmortisasis.isEmpty()
                                ? (transaction.getBunga().floatValue() / 100) * amortisasi.getNilaiBukuObligasi()
                                : (transaction.getBunga().floatValue() / 100)
                                        * tableAmortisasis.get(i - 1).getNilaiBukuObligasi();
                        tableAmortisasi.setBebanBunga(bebanBunga);

                        // Diskonto
                        Double aPremium = tableAmortisasis.isEmpty()
                                ? (amortisasi.getBungaDibayarkan() - tableAmortisasi.getBebanBunga())
                                : (amortisasi.getBungaDibayarkan() - tableAmortisasis.get(i - 1).getBebanBunga());
                        tableAmortisasi.setAmortisasiDiskonto(aPremium);


                        System.out.println("Mana Lebih Besar: "+(amortisasi.getYangBelumAmortisasi() > tableAmortisasi.getAmortisasiDiskonto()));
                        // Belum Amortisasi
                        Double premiumYangBelum = tableAmortisasis.isEmpty()
                                ? (amortisasi.getYangBelumAmortisasi() - tableAmortisasi.getAmortisasiDiskonto())
                                : (tableAmortisasis.get(i - 1).getYangBelumAmortisasi()
                                        - tableAmortisasi.getAmortisasiDiskonto());
                        tableAmortisasi.setYangBelumAmortisasi(premiumYangBelum);

                        // Nilai Buku Obligasi
                        Double nilaiBuku = tableAmortisasis.isEmpty()
                                ? (journal.getKPenerbitanUtangObligasi() + tableAmortisasi.getYangBelumAmortisasi())
                                : (journal.getKPenerbitanUtangObligasi()
                                        + tableAmortisasis.get(i - 1).getYangBelumAmortisasi());
                        tableAmortisasi.setNilaiBukuObligasi(nilaiBuku);
                    }

                    tableAmortisasis.add(tableAmortisasi);

                    
                }
                transaction.setAmortisasis(tableAmortisasis);
            }
            amortisasis.add(transaction);
        }
        model.addAttribute("amortisasis", amortisasis);

        return "amortisasi";

    }
}
