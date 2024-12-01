package com.hilwa.obligasi.service;

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
            journal.setKPenerbitan(kPenerbitan);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * transaction.getBunga() * 6 / 12;
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * transaction.getBunga() * 6 / 12;
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * transaction.getBunga() * 6 / 12;
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * transaction.getBunga() * 6 / 12;
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = dBayarBungaAwal
                    - ((kPenerbitan + kPenerbitanUtangObligasi) * transaction.getSukuBungaPasar());
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = dBayarBungaAwal
                    - ((kPenerbitan + kPenerbitanUtangObligasi) * transaction.getSukuBungaPasar());
            journal.setKAkhirTahun(kAkhirTahun);

            Double dPelunasan=transaction.getNilaiNominal() * transaction.getLembarUtang()
            * transaction.getKurs() / 100;
            journal.setDPelunasan(dPelunasan);
            Double kPelunasan = transaction.getNilaiNominal() * transaction.getLembarUtang()            
            * transaction.getKurs() / 100;
            journal.setKPelunasan(kPelunasan);

        } else {
            Double dPenerbitanKas = transaction.getNilaiNominal() * transaction.getLembarUtang()
                    * transaction.getKurs() / 100;
            journal.setDPenerbitanKas(dPenerbitanKas);

            Double kPenerbitanUtangObligasi = transaction.getNilaiNominal() * transaction.getLembarUtang();
            journal.setKPenerbitanUtangObligasi(kPenerbitanUtangObligasi);

            Double dPenerbitan = kPenerbitanUtangObligasi - dPenerbitanKas;
            journal.setDPenerbitan(dPenerbitan);
            Double kPenerbitan = kPenerbitanUtangObligasi - dPenerbitanKas;
            journal.setKPenerbitan(kPenerbitan);

            Double dBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * transaction.getBunga() * 6 / 12;
            journal.setDBayarBungaAwal(dBayarBungaAwal);
            Double kBayarBungaAwal = journal.getKPenerbitanUtangObligasi() * transaction.getBunga() * 6 / 12;
            journal.setKBayarBungaAwal(kBayarBungaAwal);

            Double dBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * transaction.getBunga() * 6 / 12;
            journal.setDBayarBungaAkhir(dBayarBungaAkhir);
            Double kBayarBungaAkhir = journal.getKPenerbitanUtangObligasi() * transaction.getBunga() * 6 / 12;
            journal.setKBayarBungaAkhir(kBayarBungaAkhir);

            Double dAkhirTahun = dBayarBungaAwal
                    - ((kPenerbitan + kPenerbitanUtangObligasi) * transaction.getSukuBungaPasar());
            journal.setDAkhirTahun(dAkhirTahun);
            Double kAkhirTahun = dBayarBungaAwal
                    - ((kPenerbitan + kPenerbitanUtangObligasi) * transaction.getSukuBungaPasar());
            journal.setKAkhirTahun(kAkhirTahun);

            Double dPelunasan=transaction.getNilaiNominal() * transaction.getLembarUtang()
            * transaction.getKurs() / 100;
            journal.setDPelunasan(dPelunasan);
            Double kPelunasan = transaction.getNilaiNominal() * transaction.getLembarUtang()            
            * transaction.getKurs() / 100;
            journal.setKPelunasan(kPelunasan);
        }
        journal = journalRepository.save(journal);
        return journalRepository.findById(id).orElse(null);
    }
}