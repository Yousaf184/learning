package com.ysf.spring6.rest.mvc.entity;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.converter.BeerStyleConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "beer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Version
    @Column(name = "opt_lock")
    private Integer version;

    @Column(name = "beer_name")
    private String beerName;

    @Column(name = "beer_style")
    @Convert(converter = BeerStyleConverter.class)
    private BeerStyle beerStyle;

    @Column(name = "upc")
    private String upc;

    @Column(name = "quantity_on_hand")
    private Integer quantityOnHand;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updateDate;
}