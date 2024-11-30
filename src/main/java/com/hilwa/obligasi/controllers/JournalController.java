package com.hilwa.obligasi.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.hilwa.obligasi.models.Journal;
import com.hilwa.obligasi.models.Transaction;
import com.hilwa.obligasi.service.JournalService;
import com.hilwa.obligasi.service.TransactionService;

@Controller
public class JournalController {
    @Autowired
    JournalService journalService;
    TransactionService transactionService;

    @GetMapping("journal")
    public String journal(Model model) {
        List<Journal> journals = journalService.getAllJournal();
        model.addAttribute("journal", journals);
        return "journal";
    }

    @GetMapping("list-journal")
    public String listjournal(Model model) {
        List<Transaction> transactions = transactionService.getAllTransaction();
        model.addAttribute("transaction", transactions);
        return "list-journal";
    }

    @GetMapping("/journal/{id}")
    public String addJurnal(Model model) {
        List<Journal> jurnal = journalService.getAllJournal();
        model.addAttribute("journal", jurnal);
        return "journal";
    }   
}
