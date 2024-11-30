package com.hilwa.obligasi.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.hilwa.obligasi.models.Transaction;
import com.hilwa.obligasi.service.TransactionService;
@Controller
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("home")
    public String home(Model model) {
        List<Transaction> transaksi = transactionService.getAllTransaction();
        model.addAttribute("transaction", transaksi);
        return "home";
    }

    @GetMapping("add-transaction")
    public String addTransaksi(Model model) {
        Transaction transaksi = new Transaction();
        model.addAttribute("transaction", transaksi);
        return "add-transaction";
    }

    @PostMapping("save-transaksi")
    public String saveTransaksi(@ModelAttribute("transaksi") Transaction transactions) {
        transactionService.save(transactions);
        return "redirect:/journal";
    }

    @GetMapping("/delete-transaction/{id}")
    public String deleteTransaksi(@PathVariable("id") Integer id) {
        transactionService.deleteById(id);
        return "redirect:/home";
    }

    @GetMapping("/update-transaction/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Transaction transaksi = transactionService.findById(id);
        model.addAttribute("transaction", transaksi);
        return "update-transaction";
    }

    @PostMapping("/update-transaction/{id}")
    public String updateTransaction(@PathVariable("id") Integer id,
            @ModelAttribute("transaction") Transaction transaksi) {
        Transaction update = transactionService.findById(id);
        if (update != null) {
            update.setNamaPerusahaan(transaksi.getNamaPerusahaan());
            update.setTanggalTerbit(transaksi.getTanggalTerbit());
            update.setNilaiNominal(transaksi.getNilaiNominal());
            update.setLembarUtang(transaksi.getLembarUtang());
            update.setBunga(transaksi.getBunga());
            update.setTanggalBayar(transaksi.getTanggalBayar());
            update.setJangkaWaktu(transaksi.getJangkaWaktu());
            update.setKurs(transaksi.getKurs());
            update.setHargaPenerbitan(transaksi.getHargaPenerbitan());
            transactionService.save(update);
        }
        return "redirect:/home";
    }
}
