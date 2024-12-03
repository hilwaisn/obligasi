package com.hilwa.obligasi.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hilwa.obligasi.models.Journal;
import com.hilwa.obligasi.models.Transaction;
import com.hilwa.obligasi.repositories.JournalRepository;

@Service
public class JournalService {
    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private TransactionService transactionService;

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

        if (transaction.getKurs() > 100) {
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang()
                    * transaction.getKurs() / 100;
            journal.setDPenerbitanKas(dPenerbitanKas);
            Double kPenerbitanUtangObligasi = transaction.getNilaiNominal() * transaction.getLembarUtang();
            journal.setKPenerbitanUtangObligasi(kPenerbitanUtangObligasi);
            Double kPenerbitan = kPenerbitanUtangObligasi - dPenerbitanKas;
            journal.setKPenerbitanPremi(kPenerbitan);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * transaction.getBunga()/2 * 6 / 12;
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * transaction.getBunga()/2 * 6 / 12;
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * transaction.getBunga()/2 * 6 / 12;
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * transaction.getBunga()/2 * 6 / 12;
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = dBayarBungaAwal
                    - ((kPenerbitan + kPenerbitanUtangObligasi) * transaction.getSukuBungaPasar());
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = dBayarBungaAwal
                    - ((kPenerbitan + kPenerbitanUtangObligasi) * transaction.getSukuBungaPasar());
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

        } else {
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang()
                    * transaction.getKurs() / 100;
            journal.setDPenerbitanKas(dPenerbitanKas);
            Double kPenerbitanUtangObligasi = transaction.getNilaiNominal() * transaction.getLembarUtang();
            Double dPenerbitan = kPenerbitanUtangObligasi - dPenerbitanKas;
            journal.setDPenerbitanDiskon(dPenerbitan);
            Double kPenerbitan = kPenerbitanUtangObligasi - dPenerbitanKas;
            journal.setKPenerbitanUtangObligasi(kPenerbitan);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * transaction.getBunga()/2 * 6 / 12;
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * transaction.getBunga()/2 * 6 / 12;
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * transaction.getBunga()/2 * 6 / 12;
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * transaction.getBunga()/2 * 6 / 12;
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = dBayarBungaAwal - (dPenerbitanKas * transaction.getSukuBungaPasar());
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = dBayarBungaAkhir - (dPenerbitanKas * transaction.getSukuBungaPasar());
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
        journal = journalRepository.save(journal);
        return journalRepository.findById(id).orElse(null);
    }}