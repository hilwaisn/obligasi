package com.hilwa.obligasi.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.hilwa.obligasi.models.Amortisasi;
import com.hilwa.obligasi.service.AmortisasiService;

@Controller
public class AmortisasiController {
    @Autowired
    AmortisasiService amortisasiService;

    @GetMapping("amortisasi")
    public String amortisasi(Model model) {
        List<Amortisasi> amortisasis = amortisasiService.getAllAmortisasi();
        model.addAttribute("amortisasi", amortisasis);
        return "amortisasi";
    }
}
