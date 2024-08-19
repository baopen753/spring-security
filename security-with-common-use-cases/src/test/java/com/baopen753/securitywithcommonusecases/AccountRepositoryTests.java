package com.baopen753.securitywithcommonusecases;

import com.baopen753.securitywithcommonusecases.model.Account;
import com.baopen753.securitywithcommonusecases.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.security.auth.login.AccountNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
@Import(TestConfig.class)
public class AccountRepositoryTests {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testValidatedAuthentication() throws AccountNotFoundException {
        String username = "baokun753";
        String password = "baokun753";

        Account account = accountRepository.findAccountByUsername(username).orElseThrow(() -> new AccountNotFoundException("Not found account with username " + username));
        Assertions.assertEquals(passwordEncoder.encode(password), account.getPassword());
        System.out.println("this is encode " + passwordEncoder.encode(password));
        System.out.println("This is stored password:" + account.getPassword());

    }

}
