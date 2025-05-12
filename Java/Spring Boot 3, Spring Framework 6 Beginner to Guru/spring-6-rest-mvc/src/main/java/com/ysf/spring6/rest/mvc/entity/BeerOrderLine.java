package com.ysf.spring6.rest.mvc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "beer_order_line")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BeerOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Version
    @Column(name = "opt_lock")
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_id", referencedColumnName = "id")
    private Beer beer;

    @Column(name = "order_quantity")
    private Integer orderQuantity;

    @Column(name = "quantity_allocated")
    private Integer quantityAllocated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_order_id", referencedColumnName = "id")
    private BeerOrder beerOrder;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
