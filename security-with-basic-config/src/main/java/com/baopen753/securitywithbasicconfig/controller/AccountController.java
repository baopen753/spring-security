package com.baopen753.securitywithbasicconfig.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @GetMapping("/myAccount")
    public ResponseEntity<?> myAccount() {
        return ResponseEntity.ok("This is my account's information");
    }

}
