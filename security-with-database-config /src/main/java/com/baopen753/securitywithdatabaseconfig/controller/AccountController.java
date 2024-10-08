package com.baopen753.securitywithdatabaseconfig.controller;

import com.baopen753.securitywithdatabaseconfig.model.Account;
import com.baopen753.securitywithdatabaseconfig.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/myAccount")
    public ResponseEntity<?> myAccount() {
        UserDetails principle = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principle.getUsername();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account accountFromRequest) {
        try {
            String hashPassword = passwordEncoder.encode(accountFromRequest.getPassword());
            accountFromRequest.setPassword(hashPassword);
            Account savedAccount = userDetailsService.createAccount(accountFromRequest);
            return ResponseEntity.ok(savedAccount);
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

