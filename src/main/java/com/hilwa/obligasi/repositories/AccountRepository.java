package com.hilwa.obligasi.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hilwa.obligasi.models.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByUsernameAndPassword(String username, String password);
}
