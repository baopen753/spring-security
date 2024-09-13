package com.baopen753.securitywithauthorizationimplementing.controller;

import com.baopen753.securitywithauthorizationimplementing.config.AccountService;
import com.baopen753.securitywithauthorizationimplementing.repository.AccountsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountsRepository accountsRepository;

    public AccountController(AccountService accountService, AccountsRepository accountsRepository) {
        this.accountService = accountService;
        this.accountsRepository = accountsRepository;
    }

    @GetMapping("/myAccount")
    public ResponseEntity<?> getMyAccount() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        UserDetails userDetails = accountService.loadUserByUsername(username);
        return ResponseEntity.ok(userDetails);
    }
//
//    @GetMapping("/myAccount")
//    public ResponseEntity<?> getMyAccount(@RequestParam int customerId) {
//        Account account = accountsRepository.findByCustomerId(customerId);
//        if (account != null) {
//            return ResponseEntity.ok(account);
//        }
//        return null;
//    }

}
