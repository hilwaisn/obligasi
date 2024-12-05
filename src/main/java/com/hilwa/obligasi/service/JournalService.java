package com.hilwa.obligasi.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hilwa.obligasi.controllers.TransactionController;
import com.hilwa.obligasi.models.Journal;
import com.hilwa.obligasi.models.Transaction;
import com.hilwa.obligasi.repositories.JournalRepository;
// import com.hilwa.obligasi.repositories.TransactionRepository;

@Service
public class JournalService {
    @Autowired
    private JournalRepository journalRepository;

    // @Autowired
    // private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionController transactionController;

    public List<Journal> findJournalsByTransactionId(Integer transactionId) {
        return journalRepository.findByTransactionId(transactionId);
    }

    public Journal findJournalById(Integer id) {
        Transaction transaction = transactionService.findById(id);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }

        Journal journal = new Journal();
        journal.setTransaction(transaction);

        if (transaction.getBunga() > transaction.getSukuBungaPasar()) {
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang()
                    * transaction.getKurs() / 100;
            journal.setDPenerbitanKas(dPenerbitanKas);
            Double kPenerbitanUtangObligasi = transaction.getNilaiNominal() * transaction.getLembarUtang();
            journal.setKPenerbitanUtangObligasi(kPenerbitanUtangObligasi);
            Double kPenerbitan = dPenerbitanKas - kPenerbitanUtangObligasi;
            journal.setKPenerbitanPremi(kPenerbitan);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = (kPenerbitanUtangObligasi * (transaction.getBunga() / 100) * (6 / 12))
                    - (journal.getDPenerbitanKas() * (transaction.getSukuBungaPasar() / 100));
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = (kPenerbitanUtangObligasi * (transaction.getBunga() / 100) * (6 / 12))
                    - (journal.getDPenerbitanKas() * (transaction.getSukuBungaPasar() / 100));
            journal.setKAkhirTahun(kAkhirTahun);

            LocalDate tanggalTerbit = transaction.getTanggalTerbit().toLocalDate();
            Integer jangkaWaktu = transaction.getJangkaWaktu();
            LocalDate tanggalPelunasan = tanggalTerbit.plus(jangkaWaktu, ChronoUnit.YEARS);
            journal.setTanggalPelunasan(java.sql.Date.valueOf(tanggalPelunasan));

            Double dPelunasan = kPenerbitanUtangObligasi;
            journal.setDPelunasan(dPelunasan);
            Double dPelunasanPremi = kPenerbitan;
            journal.setDPelunasanPremi(dPelunasanPremi);
            Double kPelunasan = dPenerbitanKas;
            journal.setKPelunasan(kPelunasan);

        }else if(transaction.getKurs() == 100){
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang()
                    * transaction.getKurs() / 100;
            journal.setDPenerbitanKas(dPenerbitanKas);
            Double kPenerbitanUtangObligasi = dPenerbitanKas;
            journal.setKPenerbitanPremi(kPenerbitanUtangObligasi);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = (kPenerbitanUtangObligasi * (transaction.getBunga() / 100) * (6 / 12)) - (transaction.getSukuBungaPasar() / 100);
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = (kPenerbitanUtangObligasi * (transaction.getBunga() / 100) * (6 / 12))
                    - (transaction.getSukuBungaPasar() / 100);
            journal.setKAkhirTahun(kAkhirTahun);

            LocalDate tanggalTerbit = transaction.getTanggalTerbit().toLocalDate();
            Integer jangkaWaktu = transaction.getJangkaWaktu();
            LocalDate tanggalPelunasan = tanggalTerbit.plus(jangkaWaktu, ChronoUnit.YEARS);
            journal.setTanggalPelunasan(java.sql.Date.valueOf(tanggalPelunasan));

            Double dPelunasan = kPenerbitanUtangObligasi;
            journal.setDPelunasan(dPelunasan);
            Double kPelunasan = dPenerbitanKas;
            journal.setKPelunasan(kPelunasan);

        } else if (transaction.getBunga() < transaction.getSukuBungaPasar()) {
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang()
                    * transaction.getKurs() / 100;
            journal.setDPenerbitanKas(dPenerbitanKas);
            Double kPenerbitanUtangObligasi = transaction.getNilaiNominal() * transaction.getLembarUtang();
            Double dPenerbitan = kPenerbitanUtangObligasi - dPenerbitanKas;
            journal.setDPenerbitanDiskon(dPenerbitan);
            Double kPenerbitan = kPenerbitanUtangObligasi - dPenerbitanKas;
            journal.setKPenerbitanUtangObligasi(kPenerbitan);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * (transaction.getBunga() / 100) * (6 / 12);
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = (kPenerbitanUtangObligasi * (transaction.getBunga() / 100) * (6 / 12))
                    - (journal.getDPenerbitanKas() * (transaction.getSukuBungaPasar() / 100));
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = (kPenerbitanUtangObligasi * (transaction.getBunga() / 100) * (6 / 12))
                    - (journal.getDPenerbitanKas() * (transaction.getSukuBungaPasar() / 100));
            journal.setKAkhirTahun(kAkhirTahun);

            LocalDate tanggalTerbit = transaction.getTanggalTerbit().toLocalDate();
            Integer jangkaWaktu = transaction.getJangkaWaktu();
            LocalDate tanggalPelunasan = tanggalTerbit.plus(jangkaWaktu, ChronoUnit.YEARS);
            journal.setTanggalPelunasan(java.sql.Date.valueOf(tanggalPelunasan));

            Double dPelunasan = kPenerbitanUtangObligasi;
            journal.setDPelunasan(dPelunasan);
            Double kPelunasan = dPenerbitanKas;
            journal.setKPelunasan(kPelunasan);
            Double kPelunasanDiskon = dPenerbitan;
            journal.setKPelunasanDiskon(kPelunasanDiskon);
        }
        else{
            transactionController.addTransaksi(null); 
        }
        journal = journalRepository.save(journal);
        return journal;
    }

    public List<Journal> getAllJournal() {
        return journalRepository.findAll();
    }
}