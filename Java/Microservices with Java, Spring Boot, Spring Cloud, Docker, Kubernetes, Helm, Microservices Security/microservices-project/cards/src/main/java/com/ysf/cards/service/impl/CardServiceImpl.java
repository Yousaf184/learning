package com.ysf.cards.service.impl;

import com.ysf.cards.dto.CardDto;
import com.ysf.cards.entity.Card;
import com.ysf.cards.exception.CardAlreadyExistsException;
import com.ysf.cards.exception.NotFoundException;
import com.ysf.cards.mapper.CardMapper;
import com.ysf.cards.repository.CardRepository;
import com.ysf.cards.service.ICardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements ICardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public CardDto createCard(String mobileNumber) {
        if (mobileNumber.isBlank()) {
            throw new IllegalArgumentException("Mobile number cannot be empty");
        }

        Optional<Card> cardOptional = this.cardRepository.findByMobileNumber(mobileNumber);

        if (cardOptional.isPresent()) {
            throw new CardAlreadyExistsException();
        }

        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        final int NEW_CARD_LIMIT = 25000;
        Card newCard = Card.builder()
                .mobileNumber(mobileNumber)
                .cardNumber(String.valueOf(randomCardNumber))
                .cardType("Credit Card")
                .totalLimit(NEW_CARD_LIMIT)
                .amountUsed(0)
                .availableAmount(NEW_CARD_LIMIT)
                .build();

        Card savedCard = this.cardRepository.save(newCard);
        return cardMapper.toCardDto(savedCard);
    }

    @Override
    public CardDto getCardDetails(String mobileNumber) {
        Optional<Card> cardOptional = this.cardRepository.findByMobileNumber(mobileNumber);

        Card card = cardOptional.orElseThrow(() -> {
            String errorMsg = "No card associated with the given mobile number";
            return new NotFoundException(errorMsg);
        });

        return this.cardMapper.toCardDto(card);
    }

    @Override
    public void updateCard(CardDto updatedCardData, String cardNumber) {
        Optional<Card> cardOptional = this.cardRepository.findByCardNumber(cardNumber);

        Card cardToUpdate = cardOptional.orElseThrow(() -> {
            String errorMsg = "No card associated with the given card number";
            return new NotFoundException(errorMsg);
        });

        if (StringUtils.hasText(updatedCardData.getMobileNumber())) {
            cardToUpdate.setMobileNumber(updatedCardData.getMobileNumber());
        }
        if (updatedCardData.getTotalLimit() != null) {
            cardToUpdate.setTotalLimit(updatedCardData.getTotalLimit());
        }
        if (updatedCardData.getAmountUsed() != null) {
            cardToUpdate.setAmountUsed(updatedCardData.getAmountUsed());
        }
        if (updatedCardData.getAvailableAmount() != null) {
            cardToUpdate.setAvailableAmount(updatedCardData.getAvailableAmount());
        }

        this.cardRepository.save(cardToUpdate);
    }

    @Override
    public void deleteCard(String cardNumber) {
        Optional<Card> cardOptional = this.cardRepository.findByCardNumber(cardNumber);

        cardOptional.ifPresent(cardToDelete -> this.cardRepository.deleteById(cardToDelete.getId()));
    }
}
