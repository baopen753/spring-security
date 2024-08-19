package com.baopen753.securitywithcommonusecases.controller;

import com.baopen753.securitywithcommonusecases.model.Account;
import com.baopen753.securitywithcommonusecases.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody Account account) {
        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);
        Account newAccount = registerService.registerAccount(account);

        if(newAccount.getId() > 0)
            return ResponseEntity.status(HttpStatus.CREATED).body("Created account successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
    }
}
