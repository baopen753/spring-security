package com.baopen753.securitywithcommonusecases.controller;

import com.baopen753.securitywithcommonusecases.config.AccountService;
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

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/myAccount")
    public ResponseEntity<?> getMyAccount() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        UserDetails userDetails = accountService.loadUserByUsername(username);
        return ResponseEntity.ok(userDetails);
    }

    private String discardPasswordPrefix(String password) {
        String[] splits = password.split("}");
        return splits[1];
    }
}
