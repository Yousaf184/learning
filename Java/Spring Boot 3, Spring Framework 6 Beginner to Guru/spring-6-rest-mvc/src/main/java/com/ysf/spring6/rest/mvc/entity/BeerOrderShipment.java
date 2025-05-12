package com.ysf.spring6.rest.mvc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "beer_order_shipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BeerOrderShipment {

    @Id
    @Column(name = "id")
    private UUID id;

    @Version
    @Column(name = "opt_lock")
    private Integer version;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private BeerOrder beerOrder;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}