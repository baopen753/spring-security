package com.baopen753.securitywithauthorizationimplementing.repository;

import com.baopen753.securitywithauthorizationimplementing.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findCustomerByEmail(String username);
}
