package com.baopen753.securitywithpasswordmanagement.accounts;

import com.baopen753.securitywithpasswordmanagement.model.Account;
import com.baopen753.securitywithpasswordmanagement.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;


    @Test
    public void testFindUserByEmailSuccessful(){
        String username = "baopen753";
        Optional<Account> account = accountRepository.findByUsername(username);

        Assertions.assertTrue(account.isPresent());
        System.out.println(account);
    }

}
