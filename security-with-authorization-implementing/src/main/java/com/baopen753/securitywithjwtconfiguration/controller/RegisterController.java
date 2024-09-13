package com.baopen753.securitywithauthorizationimplementing.controller;

import com.baopen753.securitywithauthorizationimplementing.model.Customer;
import com.baopen753.securitywithauthorizationimplementing.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody Customer customer) {
        String hashedPassword = passwordEncoder.encode(customer.getPwd());
        customer.setPwd(hashedPassword);
        customer.setCreateDt(new Date());
        Customer newCustomer = registerService.registerAccount(customer);

        if(newCustomer.getId() > 0)
            return ResponseEntity.status(HttpStatus.CREATED).body("Created account successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
    }
}
