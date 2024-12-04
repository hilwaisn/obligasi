package com.hilwa.obligasi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.hilwa.obligasi.models.Account;
import com.hilwa.obligasi.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("registrasi")
    public String registrasi(Model model) {
        Account akun = new Account();
        model.addAttribute("regis", akun);
        return "registrasi";
    }

    @PostMapping("save-regis")
    public String saveRegis(@ModelAttribute("regis") Account akun, Model model) {
        // Validasi username
        if (akun.getUsername().contains(" ")) {
            model.addAttribute("usernameError", "Username tidak boleh mengandung spasi.");
        } else if (akun.getUsername().length() < 3 || akun.getUsername().length() > 10) {
            model.addAttribute("usernameError", "Username harus antara 3 hingga 10 karakter.");
        }

        // Validasi password
        if (akun.getPassword().length() < 8 || akun.getPassword().length() > 10) {
            model.addAttribute("passwordError", "Password harus antara 8 hingga 10 karakter.");
        }

        // Jika ada error pada validasi, kembalikan ke halaman registrasi
        if (model.containsAttribute("usernameError") || model.containsAttribute("passwordError")) {
            return "registrasi";
        }

        // Jika validasi berhasil, simpan data login dan redirect ke login page
        accountService.save(akun);
        return "redirect:/login";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("home")
    public String akun(@RequestParam("username") String username,
            @RequestParam("password") String password, Model model) {
        Account akun = accountService.findByUsernameAndPassword(username, password);
        if (akun != null) {
            model.addAttribute("login", akun);
            return "home";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/update-akun/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Account account = accountService.findById(id);
        model.addAttribute("login", account);
        return "update-akun";
    }

    @PostMapping("/update-akun/{id}")
    public String updateAccount(@PathVariable("id") Integer id,
            @ModelAttribute("login") Account akun) {
        Account update = accountService.findById(id);
        if (update != null) {
            update.setUsername(akun.getUsername());
            update.setPassword(akun.getPassword());
            accountService.save(update);
        }
        return "redirect:/home";
    }

    @GetMapping("about")
    public String about() {
        return "about";
    }
}
