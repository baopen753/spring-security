package com.baopen753.securitywithauthorizationimplementing.controller;

import com.baopen753.securitywithauthorizationimplementing.model.AccountTransactions;
import com.baopen753.securitywithauthorizationimplementing.repository.AccountTransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {

    private final AccountTransactionRepository accountTransactionRepository;

    public BalanceController(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }


    @GetMapping("/myBalance")
    public ResponseEntity<?> myBalance(@RequestParam int id) {
        List<AccountTransactions> accountTransactionsList = accountTransactionRepository.findByCustomerIdOrderByTransactionDtDesc(id);
        if(accountTransactionsList.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(accountTransactionsList);
    }

}
