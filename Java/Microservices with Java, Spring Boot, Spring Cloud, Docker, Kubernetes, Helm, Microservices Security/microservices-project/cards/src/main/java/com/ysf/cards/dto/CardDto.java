package com.ysf.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Card")
public class CardDto {

    private Long id;
    private String mobileNumber;
    private String cardNumber;
    private String cardType;

    @PositiveOrZero(message = "Card limit should be greater than or equal to zero")
    private Integer totalLimit;

    @PositiveOrZero(message = "Amount used should be greater than or equal to zero")
    private Integer amountUsed;

    @PositiveOrZero(message = "Available amount should be greater than or equal to zero")
    private Integer availableAmount;
}
