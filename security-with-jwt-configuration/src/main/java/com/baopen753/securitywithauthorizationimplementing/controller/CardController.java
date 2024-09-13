package com.baopen753.securitywithauthorizationimplementing.controller;

import com.baopen753.securitywithauthorizationimplementing.model.Card;
import com.baopen753.securitywithauthorizationimplementing.repository.CardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    @GetMapping("/myCard")
    public ResponseEntity<?> getCardFromCustomerId(@RequestParam Integer id) {

        List<Card> cardsList = cardRepository.findCardsByCustomerId(id);
        if (cardsList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(cardsList);

    }

    @GetMapping("/myCard/{name}")
    public ResponseEntity<?> getCardFromCustomerName(@PathVariable String name) {
        List<Card> cardsList = cardRepository.findCardsByCustomerName(name);
        if (cardsList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(cardsList);
    }

}
