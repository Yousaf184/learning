package com.ysf.spring6.rest.mvc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Customer {

    // provide custom implementation of all args constructor
    // to ensure that the bidirectional relationship is created
    // between BeerOrder and the Customer when the builder pattern
    // is used to construct Customer
    public Customer(UUID id,
                    Integer version,
                    String name,
                    LocalDateTime createdDate,
                    LocalDateTime updateDate,
                    List<BeerOrder> beerOrders) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.beerOrders = beerOrders;
        if (beerOrders != null) {
            beerOrders.forEach(bo -> bo.setCustomer(this));
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Version
    @Column(name = "opt_lock")
    private Integer version;

    @Column(name = "name")
    private String name;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BeerOrder> beerOrders;

    public void addBeerOrder(BeerOrder beerOrder) {
        if (this.beerOrders == null) {
            this.beerOrders = new ArrayList<>();
        }
        this.beerOrders.add(beerOrder);
    }

    public void removeBeerOrder(BeerOrder beerOrder) {
        if (this.beerOrders == null) {
            return;
        }
        this.beerOrders.removeIf(bo -> bo.getId().equals(beerOrder.getId()));
        beerOrder.setCustomer(null);
    }
}