package com.ysf.cards.service;

import com.ysf.cards.dto.CardDto;

public interface ICardService {

    CardDto createCard(String mobileNumber);

    CardDto getCardDetails(String mobileNumber);

    void updateCard(CardDto updatedCardData, String cardNumber);

    void deleteCard(String cardNumber);
}
