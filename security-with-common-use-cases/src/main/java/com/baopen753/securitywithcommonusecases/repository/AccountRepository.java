package com.baopen753.securitywithcommonusecases.repository;

import com.baopen753.securitywithcommonusecases.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByUsername(String username);
}
