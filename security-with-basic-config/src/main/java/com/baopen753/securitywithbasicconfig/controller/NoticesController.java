package com.baopen753.securitywithbasicconfig.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesController {

    @GetMapping("/notices")
    public ResponseEntity<?> notice() {
        return ResponseEntity.ok("This is our notice's information");
    }

}
