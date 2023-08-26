package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    private ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                              Authentication authentication){

        Client currentClient = clientRepository.findByEmail(authentication.getName());

        Card newCard = new Card(cardHolder(currentClient.getFirstName(), currentClient.getLastName()), type , color,
                    numberCard(), (int)(Math.random()*999+1), LocalDate.now(), LocalDate.now().plusYears(5));
        currentClient.addCard(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
        }



    public String cardHolder(String firstName, String lastName){
            return firstName + " " + lastName;
    }
    public String numberCard(){
        int num1 = (int)(Math.random()*9999+1);
        int num2 = (int)(Math.random()*9999+1);
        int num3 = (int)(Math.random()*9999+1);
        int num4 = (int)(Math.random()*9999+1);
        return num1 + "-" + num2 + "-" + num3 + "-" + num4;
    }
}
