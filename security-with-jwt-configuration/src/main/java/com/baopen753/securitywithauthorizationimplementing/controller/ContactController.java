package com.baopen753.securitywithauthorizationimplementing.controller;


import com.baopen753.securitywithauthorizationimplementing.model.Contact;
import com.baopen753.securitywithauthorizationimplementing.repository.ContactRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class ContactController {

    private final ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }


    @PostMapping("/contact")
    public ResponseEntity<?> contact(@RequestBody Contact contact) {
        contact.setContactId(getRandomContactId());
        Contact savedContact = contactRepository.save(contact);
        return ResponseEntity.ok(savedContact);
    }

    private String getRandomContactId() {
        Random random = new Random();
        return "CM" + random.nextInt(900000000) + 1000000;   // ranging:  10000000 -> 999999999
    }

}
