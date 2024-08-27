package com.baopen753.securitywithcustomizedauthenticationprovider.service;

import com.baopen753.securitywithcustomizedauthenticationprovider.model.Account;
import com.baopen753.securitywithcustomizedauthenticationprovider.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final AccountRepository accountRepository;

    public Account registerAccount(Account accountInput) {
        return accountRepository.save(accountInput);
    }
}
