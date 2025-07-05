package com.ysf.cards.mapper;

import com.ysf.cards.dto.CardDto;
import com.ysf.cards.entity.Card;
import org.mapstruct.Mapper;

@Mapper
public interface CardMapper {

//    Card toCardEntity(CardDto cardDto);

    CardDto toCardDto(Card card);
}
