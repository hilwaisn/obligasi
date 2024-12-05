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
        Journal cek = journalRepository.getByTransaction(transaction);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }

        Journal journal = new Journal();
        journal.setTransaction(transaction);

        Double nilaiBunga = (transaction.getBunga().floatValue() / 100.0) * (6.0 / 12.0);
        
        if (transaction.getBunga() > transaction.getSukuBungaPasar()) {
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang().floatValue()
                    * transaction.getKurs().floatValue() / 100.0;
            journal.setDPenerbitanKas(dPenerbitanKas);
            Double kPenerbitanUtangObligasi = transaction.getNilaiNominal() * transaction.getLembarUtang();
            journal.setKPenerbitanUtangObligasi(kPenerbitanUtangObligasi);
            Double kPenerbitan = dPenerbitanKas - kPenerbitanUtangObligasi;
            journal.setKPenerbitanPremi(kPenerbitan);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = (journal.getDBayarBungaAwal()+journal.getDBayarBungaAkhir())-((transaction.getSukuBungaPasar().floatValue() / 100.0)*journal.getDPenerbitanKas());
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = (journal.getDBayarBungaAwal()+journal.getDBayarBungaAkhir())-((transaction.getSukuBungaPasar().floatValue() / 100.0)*journal.getDPenerbitanKas());
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

        } else if (transaction.getBunga() < transaction.getSukuBungaPasar()) {
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang().floatValue()
                    * transaction.getKurs().floatValue() / 100.0;
            journal.setDPenerbitanKas(dPenerbitanKas);

            Double kPenerbitanUtangObligasi = transaction.getNilaiNominal() * transaction.getLembarUtang().floatValue();
            Double kPenerbitan = kPenerbitanUtangObligasi;
            journal.setKPenerbitanUtangObligasi(kPenerbitan);

            Double dPenerbitan = kPenerbitanUtangObligasi - dPenerbitanKas;
            journal.setDPenerbitanDiskon(dPenerbitan);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = ((transaction.getSukuBungaPasar().floatValue() / 100.0)*journal.getDPenerbitanKas())-(journal.getDBayarBungaAwal()+journal.getDBayarBungaAkhir());
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = ((transaction.getSukuBungaPasar().floatValue() / 100.0)*journal.getDPenerbitanKas())-(journal.getDBayarBungaAwal()+journal.getDBayarBungaAkhir());
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
        } else if(transaction.getKurs() == 100){
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang().floatValue()
                    * transaction.getKurs().floatValue() / 100.;
            journal.setDPenerbitanKas(dPenerbitanKas);
            Double kPenerbitanUtangObligasi = dPenerbitanKas;
            journal.setKPenerbitanPremi(kPenerbitanUtangObligasi);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * nilaiBunga;
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = (journal.getDBayarBungaAwal()+journal.getDBayarBungaAkhir())-((transaction.getSukuBungaPasar().floatValue() / 100.0)*journal.getDPenerbitanKas());
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = (journal.getDBayarBungaAwal()+journal.getDBayarBungaAkhir())-((transaction.getSukuBungaPasar().floatValue() / 100.0)*journal.getDPenerbitanKas());
            journal.setKAkhirTahun(kAkhirTahun);

            LocalDate tanggalTerbit = transaction.getTanggalTerbit().toLocalDate();
            Integer jangkaWaktu = transaction.getJangkaWaktu();
            LocalDate tanggalPelunasan = tanggalTerbit.plus(jangkaWaktu, ChronoUnit.YEARS);
            journal.setTanggalPelunasan(java.sql.Date.valueOf(tanggalPelunasan));

            Double dPelunasan = kPenerbitanUtangObligasi;
            journal.setDPelunasan(dPelunasan);
            Double kPelunasan = dPenerbitanKas;
            journal.setKPelunasan(kPelunasan);
        }
        
        if (cek == null) {
            journalRepository.save(journal);
        }

        return journal;
    }

    public List<Journal> getAllJournal() {
        return journalRepository.findAll();
    }
}