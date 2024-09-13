package com.baopen753.securitywithauthorizationimplementing.repository;

import com.baopen753.securitywithauthorizationimplementing.model.AccountTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountTransactionRepository extends JpaRepository<AccountTransactions, Integer> {
    List<AccountTransactions> findByCustomerIdOrderByTransactionDtDesc(long customerId);
}
