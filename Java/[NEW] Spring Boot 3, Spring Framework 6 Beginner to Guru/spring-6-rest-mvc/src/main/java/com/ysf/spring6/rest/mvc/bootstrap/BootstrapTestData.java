package com.ysf.spring6.rest.mvc.bootstrap;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.entity.Beer;
import com.ysf.spring6.rest.mvc.entity.Customer;
import com.ysf.spring6.rest.mvc.repository.BeerRepository;
import com.ysf.spring6.rest.mvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Profile("test")
@Slf4j
@RequiredArgsConstructor
public class BootstrapTestData implements CommandLineRunner {
    
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    
    @Override
    public void run(String... args) {
        log.info("Populating test data");

        this.populateBeerData();
        this.populateCustomerData();
    }

    private void populateBeerData() {
        if (this.beerRepository.count() > 0) {
            return;
        }

        log.info("Populating beer test data");

        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));
    }

    private void populateCustomerData() {
        if (this.customerRepository.count() > 0) {
            return;
        }

        log.info("Populating customer test data");

        Customer customer1 = Customer.builder()
                .name("Customer 1")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .name("Customer 2")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .name("Customer 3")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
    }
}
