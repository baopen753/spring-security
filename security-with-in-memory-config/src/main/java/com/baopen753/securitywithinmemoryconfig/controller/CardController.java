package com.baopen753.securitywithinmemoryconfig.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    @GetMapping("/myCard")
    public ResponseEntity<?> myCard() {
        return ResponseEntity.ok("This is my card's information");
    }

}

