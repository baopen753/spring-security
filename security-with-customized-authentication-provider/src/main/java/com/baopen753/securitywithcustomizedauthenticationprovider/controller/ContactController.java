package com.baopen753.securitywithcustomizedauthenticationprovider.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @GetMapping("/contact")
    public ResponseEntity<?> contact() {
        return ResponseEntity.ok("This is our contact's information");
    }


}
