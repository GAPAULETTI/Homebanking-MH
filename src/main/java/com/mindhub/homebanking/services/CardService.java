package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface CardService {

    void saveCard(Card card);

    Card getCardById(Long id);

    CardDTO getCardDTO(Long id);

    Set<CardDTO> getCards(Authentication authentication);

    Card findByCardNumber(String cardNumber);

}
