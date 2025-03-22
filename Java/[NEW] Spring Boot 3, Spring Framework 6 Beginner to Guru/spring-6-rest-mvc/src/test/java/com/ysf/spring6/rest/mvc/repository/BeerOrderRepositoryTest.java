package com.ysf.spring6.rest.mvc.repository;

import com.ysf.spring6.rest.mvc.entity.Beer;
import com.ysf.spring6.rest.mvc.entity.BeerOrder;
import com.ysf.spring6.rest.mvc.entity.BeerOrderShipment;
import com.ysf.spring6.rest.mvc.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
class BeerOrderRepositoryTest {

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BeerRepository beerRepository;

    private Customer testCustomer;
    private Beer testBeer;

    @BeforeEach
    void setUp() {
        this.testCustomer = customerRepository.findAll().getFirst();
        this.testBeer = beerRepository.findAll().getFirst();
    }

    @Transactional
    @Commit
    @Test
    void testBeerOrders() {
        BeerOrderShipment beerOrderShipment = BeerOrderShipment.builder()
                .trackingNumber("1235r")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .beerOrderShipment(beerOrderShipment)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        System.out.println(savedBeerOrder.getId());
        System.out.println(savedBeerOrder.getCustomerRef());
    }
}