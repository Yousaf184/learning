package com.ysf.spring6.rest.mvc.bootstrap;

import com.opencsv.bean.CsvToBeanBuilder;
import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.dto.BeerCsvRecord;
import com.ysf.spring6.rest.mvc.entity.Beer;
import com.ysf.spring6.rest.mvc.entity.Customer;
import com.ysf.spring6.rest.mvc.repository.BeerRepository;
import com.ysf.spring6.rest.mvc.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("test")
@Slf4j
@RequiredArgsConstructor
public class BootstrapTestData implements CommandLineRunner {
    
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    @Override
    public void run(String... args) throws FileNotFoundException {
        log.info("Populating test data");

        this.populateBeerData();
        this.populateCustomerData();
        this.populateCsvData();
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

    private void populateCsvData() throws FileNotFoundException {
        if (this.beerRepository.count() > 10) {
            return;
        }

        File csvFile = ResourceUtils.getFile("classpath:beerCsvData/beers.csv");

        List<BeerCsvRecord> beerCsvRecords = new CsvToBeanBuilder<BeerCsvRecord>(new FileReader(csvFile))
                .withType(BeerCsvRecord.class)
                .build().parse();

        beerCsvRecords.forEach(beerCSVRecord -> {
            BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                case "American Pale Lager" -> BeerStyle.LAGER;
                case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" -> BeerStyle.ALE;
                case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                case "American Porter" -> BeerStyle.PORTER;
                case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                case "English Pale Ale" -> BeerStyle.PALE_ALE;
                default -> BeerStyle.PILSNER;
            };

            Beer beer = Beer.builder()
                    .beerName(beerCSVRecord.getBeer())
                    .beerStyle(beerStyle)
                    .price(BigDecimal.TEN)
                    .upc(beerCSVRecord.getRow().toString())
                    .quantityOnHand(beerCSVRecord.getCount())
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.save(beer);
        });
    }
}
