package com.baopen753.securitywithauthorizationimplementing.service;

import com.baopen753.securitywithauthorizationimplementing.model.Customer;
import com.baopen753.securitywithauthorizationimplementing.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final CustomerRepository customerRepository;

    public Customer registerAccount(Customer customerInput) {
        return customerRepository.save(customerInput);
    }
}
