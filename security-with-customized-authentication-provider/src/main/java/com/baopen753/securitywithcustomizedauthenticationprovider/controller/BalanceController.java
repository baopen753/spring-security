package com.baopen753.securitywithcustomizedauthenticationprovider.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    @GetMapping("/myBalance")
    public ResponseEntity<?> balance() {
        return ResponseEntity.ok("This is my balance number");
    }

}
