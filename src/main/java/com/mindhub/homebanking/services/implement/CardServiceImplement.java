package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientService clientService;

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public CardDTO getCardDTO(Long id) {

        return new CardDTO(this.getCardById(id));

    }

    @Override
    public Set<CardDTO> getCards(Authentication authentication) {
        Client currentClient = clientService.getByAuth(authentication);
        return currentClient.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    @Override
    public Card findByCardNumber(String cardNumber) {

        return cardRepository.findByCardNumber(cardNumber);

    }




}
