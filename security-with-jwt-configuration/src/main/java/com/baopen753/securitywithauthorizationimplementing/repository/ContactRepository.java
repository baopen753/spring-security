package com.baopen753.securitywithauthorizationimplementing.repository;

import com.baopen753.securitywithauthorizationimplementing.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
