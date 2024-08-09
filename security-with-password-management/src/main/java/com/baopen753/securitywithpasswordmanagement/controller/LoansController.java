package com.baopen753.securitywithpasswordmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    @GetMapping("/myLoans")
    public ResponseEntity<?> myLoans() {
        return ResponseEntity.ok("This is my loan's information");
    }

}
