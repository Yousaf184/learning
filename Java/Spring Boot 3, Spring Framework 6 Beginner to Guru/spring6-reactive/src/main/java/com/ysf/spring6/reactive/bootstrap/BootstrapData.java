package com.ysf.spring6.reactive.bootstrap;

import com.ysf.spring6.reactive.model.Beer;
import com.ysf.spring6.reactive.model.Customer;
import com.ysf.spring6.reactive.repository.BeerRepository;
import com.ysf.spring6.reactive.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        loadBeerData();
        loadCustomerData();

        this.beerRepository.count()
                .subscribe(count -> System.out.println("Beer count: " + count));
        this.customerRepository.count()
                .subscribe(count -> System.out.println("Customer count: " + count));
    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                List<Beer> testBeerData = BootstrapData.createTestBeerData();
                beerRepository.save(testBeerData.get(0)).subscribe();
                beerRepository.save(testBeerData.get(1)).subscribe();
                beerRepository.save(testBeerData.get(2)).subscribe();
            }
        });
    }

    private void loadCustomerData() {
        this.customerRepository.count().subscribe(count -> {
            if (count == 0) {
                List<Customer> testCustomerData = BootstrapData.createTestCustomerData();
                customerRepository.save(testCustomerData.get(0)).subscribe();
                customerRepository.save(testCustomerData.get(1)).subscribe();
                customerRepository.save(testCustomerData.get(2)).subscribe();
            }
        });
    }

    public static List<Beer> createTestBeerData() {
        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle("Pale Ale")
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Crank")
                .beerStyle("Pale Ale")
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Sunshine City")
                .beerStyle("IPA")
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        return List.of(beer1, beer2, beer3);
    }

    public static List<Customer> createTestCustomerData() {
        Customer customer1 = Customer.builder()
                .name("Customer 1")
                .build();

        Customer customer2 = Customer.builder()
                .name("Customer 2")
                .build();

        Customer customer3 = Customer.builder()
                .name("Customer 3")
                .build();

        return List.of(customer1, customer2, customer3);
    }
}
