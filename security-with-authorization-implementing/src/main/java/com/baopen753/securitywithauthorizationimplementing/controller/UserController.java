package com.baopen753.securitywithauthorizationimplementing.controller;

import com.baopen753.securitywithauthorizationimplementing.model.Customer;
import com.baopen753.securitywithauthorizationimplementing.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private final CustomerRepository customerRepository;

    public UserController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/user")
    public ResponseEntity<Customer> getUserAfterLogin(Authentication authentication) {
        Optional<Customer> user =  customerRepository.findCustomerByEmail(authentication.getName());
        return ResponseEntity.ok(user.orElse(null));
    }

}
