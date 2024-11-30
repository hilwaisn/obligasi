package com.hilwa.obligasi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hilwa.obligasi.models.Account;
import com.hilwa.obligasi.repositories.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public void save(Account login) {
        accountRepository.save(login);
    }

    public Account findByUsernameAndPassword(String username, String password) {
        List<Account> login = accountRepository.findByUsernameAndPassword(username, password);
        return login.stream()
                .findFirst()
                .orElse(null);
    }

    public List<Account> getAllLogin() {
        return accountRepository.findAll();
    }

    public Account findById(Integer id) {
        return accountRepository.findById(id).orElse(null);
    }
}
