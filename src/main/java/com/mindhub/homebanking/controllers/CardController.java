package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository repoClient;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam @NotNull CardType cardType, @RequestParam @NotNull CardColor cardColor,
                                             Authentication authentication) {

        Client currentClient = clientService.getByAuth(authentication);

        Set<Card> limitDebitCard = currentClient.getCards().stream().filter(card -> card.getType().equals(CardType.DEBIT)).collect(Collectors.toSet());
        Set<Card> limitCreditCard = currentClient.getCards().stream().filter(card -> card.getType().equals(CardType.CREDIT)).collect(Collectors.toSet());
        if(cardColor.equals(null) || cardColor.equals(null)){
            return new ResponseEntity<>("You must choose type and color card", HttpStatus.FORBIDDEN);
        }
        if (cardType.equals(CardType.DEBIT) && limitDebitCard.size() >= 3) {
            return new ResponseEntity<>("The limit has been exceed", HttpStatus.FORBIDDEN);
        }
        if (cardType.equals(CardType.CREDIT) && limitCreditCard.size() >= 3 ) {
            return new ResponseEntity<>("The limit has been exceed",HttpStatus.FORBIDDEN);
        }

        String numberCard = generateNumberCard();

        if(generateNumberCard().equals(cardService.findByCardNumber(generateNumberCard()))) {
            return new ResponseEntity<>("This account number exists", HttpStatus.FORBIDDEN);
        }else {
            Card newCard = new Card(cardHolder(currentClient.getFirstName(), currentClient.getLastName()), cardType, cardColor,
                    numberCard, cvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5));
            currentClient.addCard(newCard);
            cardService.saveCard(newCard);
            clientService.saveClient(currentClient);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        }




    public String cardHolder(String firstName, String lastName){
            return firstName + " " + lastName;
    }
    public String generateNumberCard(){
        int max = 9999;
        int min = 1000;
        int num1 = (int) (1000 + (Math.random()*8999));
        int num2 = (int) (1000 + (Math.random()*8999));
        int num3 = (int) (1000 + (Math.random()*8999));
        int num4 = (int) (1000 + (Math.random()*8999));
        return num1 + " " + num2 + " " + num3 + " " + num4;
    }
    public int cvvNumber(){
        return (int)(100 + (Math.random()*999));
    }
}
