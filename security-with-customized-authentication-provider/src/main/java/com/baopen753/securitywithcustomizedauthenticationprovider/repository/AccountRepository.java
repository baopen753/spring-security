package com.baopen753.securitywithcustomizedauthenticationprovider.repository;

import com.baopen753.securitywithcustomizedauthenticationprovider.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);   // Spring JPA understands how the operation worked then execute queries at runtime
}
