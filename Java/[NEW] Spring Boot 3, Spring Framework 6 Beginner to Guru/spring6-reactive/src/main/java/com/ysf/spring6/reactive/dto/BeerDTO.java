package com.ysf.spring6.reactive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerDTO {
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String beerName;

    @Size(min = 1, max = 255)
    private String beerStyle;

    @Size(min = 1, max = 25)
    private String upc;

    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}

