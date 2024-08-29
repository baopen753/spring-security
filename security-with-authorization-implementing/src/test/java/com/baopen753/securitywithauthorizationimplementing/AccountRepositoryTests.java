package com.baopen753.securitywithauthorizationimplementing;

import com.baopen753.securitywithauthorizationimplementing.model.Card;
import com.baopen753.securitywithauthorizationimplementing.model.Customer;
import com.baopen753.securitywithauthorizationimplementing.model.Loans;
import com.baopen753.securitywithauthorizationimplementing.repository.CardRepository;
import com.baopen753.securitywithauthorizationimplementing.repository.CustomerRepository;
import com.baopen753.securitywithauthorizationimplementing.repository.LoanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
@Import(TestConfig.class)
public class AccountRepositoryTests {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testValidatedAuthentication() throws AccountNotFoundException {
        String username = "baokun753";
        String password = "baokun753";

        Customer customer = customerRepository.findCustomerByEmail(username).orElseThrow(() -> new AccountNotFoundException(username));
        Assertions.assertEquals(passwordEncoder.encode(password), customer.getPwd());
        System.out.println("this is encode " + passwordEncoder.encode(password));
        System.out.println("This is stored password:" + customer.getPwd());

    }

    @Test
    public void testGetLoansByCustomerId() {
        int customerId = 1;
        List<Loans> loansList = loanRepository.findByCustomerIdOrderByStartDtDesc(customerId);
        Assertions.assertNotNull(loansList);

        loansList.forEach(System.out::println);
    }

    @Test
    public void testGetCardsWithCustomerName() {
        String customerName = "baopen";
        List<Card> loansList = cardRepository.findCardsByCustomerName(customerName);
        Assertions.assertNotNull(loansList);

        loansList.forEach(System.out::println);
    }

}
