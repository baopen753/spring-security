package com.baopen753.securitywithauthorizationimplementing.controller;

import com.baopen753.securitywithauthorizationimplementing.model.Loans;
import com.baopen753.securitywithauthorizationimplementing.repository.LoanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoansController {

    private final LoanRepository loanRepository;

    public LoansController(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @GetMapping("/myLoans")
    public ResponseEntity<?> myLoans(@RequestParam int id) {
       List<Loans> loansList = loanRepository.findByCustomerIdOrderByStartDtDesc(id);
       if (loansList.isEmpty()){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(loansList);
    }
}
