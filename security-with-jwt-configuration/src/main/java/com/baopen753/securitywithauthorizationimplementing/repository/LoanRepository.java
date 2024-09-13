package com.baopen753.securitywithauthorizationimplementing.repository;

import com.baopen753.securitywithauthorizationimplementing.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LoanRepository extends JpaRepository<Loans, Integer> {
    List<Loans> findByCustomerIdOrderByStartDtDesc(long customerId);
}
