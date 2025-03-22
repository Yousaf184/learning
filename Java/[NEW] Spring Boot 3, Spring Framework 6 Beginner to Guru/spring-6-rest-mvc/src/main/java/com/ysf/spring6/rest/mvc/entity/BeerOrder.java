package com.ysf.spring6.rest.mvc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "beer_order")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BeerOrder {

    // provide custom implementation of all args constructor
    // to ensure that the bidirectional relationship is created
    // between BeerOrderLine and the BeerOrder when the builder
    // pattern is used to construct BeerOrder
    public BeerOrder(UUID id,
                     Integer version,
                     String customerRef,
                     Customer customer,
                     LocalDateTime createdDate,
                     LocalDateTime lastModifiedDate,
                     List<BeerOrderLine> beerOrderLines,
                     BeerOrderShipment beerOrderShipment) {
        this.id = id;
        this.version = version;
        this.customerRef = customerRef;
        this.customer = customer;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        if (beerOrderLines != null) {
            beerOrderLines.forEach(this::addBeerOrderLine);
        }
        if (beerOrderShipment != null) {
            this.setBeerOrderShipment(beerOrderShipment);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Version
    @Column(name = "opt_lock")
    private Integer version;

    @Column(name = "customer_ref")
    private String customerRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "beerOrder", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BeerOrderLine> beerOrderLines;

    @OneToOne(mappedBy = "beerOrder", cascade = CascadeType.ALL)
    private BeerOrderShipment beerOrderShipment;

    public void addBeerOrderLine(BeerOrderLine beerOrderLine) {
        if (this.beerOrderLines == null) {
            this.beerOrderLines = new ArrayList<>();
        }
        this.beerOrderLines.add(beerOrderLine);
        beerOrderLine.setBeerOrder(this);
    }

    public void removeBeerOrderLine(BeerOrderLine beerOrderLine) {
        if (this.beerOrderLines == null) {
            return;
        }
        this.beerOrderLines.removeIf(bol -> bol.getId().equals(beerOrderLine.getId()));
        beerOrderLine.setBeerOrder(null);
    }

    public void setBeerOrderShipment(BeerOrderShipment beerOrderShipment) {
        this.beerOrderShipment = beerOrderShipment;
        beerOrderShipment.setBeerOrder(this);
    }
}
