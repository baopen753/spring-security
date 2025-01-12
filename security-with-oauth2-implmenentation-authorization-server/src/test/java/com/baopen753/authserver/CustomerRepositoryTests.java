package com.baopen753.authserver;

import com.baopen753.authserver.model.Customer;
import com.baopen753.authserver.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testGetCustomerByEmail() {
        String email = "happy@example.com";
        String password = "12345";
        Optional<Customer> customer = customerRepository.findByEmail(email);

        if (customer.get().getPwd().equals(password)) {
            System.out.println(customer.get().getEmail());
        }

        Assertions.assertThat(customer.get()).isNotNull();

    }
}
