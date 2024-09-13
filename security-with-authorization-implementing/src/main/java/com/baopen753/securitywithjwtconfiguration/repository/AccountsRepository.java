package com.baopen753.securitywithauthorizationimplementing.repository;

import com.baopen753.securitywithauthorizationimplementing.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, Integer> {

    Account findByCustomerId(int customerId);

}
