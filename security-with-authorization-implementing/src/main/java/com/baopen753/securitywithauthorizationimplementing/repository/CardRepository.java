package com.baopen753.securitywithauthorizationimplementing.repository;

import com.baopen753.securitywithauthorizationimplementing.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findCardsByCustomerId(int customerId);

    @Query("select c from Card c where c.customer.name = ?1")
    List<Card> findCardsByCustomerName(String customerName);
}
