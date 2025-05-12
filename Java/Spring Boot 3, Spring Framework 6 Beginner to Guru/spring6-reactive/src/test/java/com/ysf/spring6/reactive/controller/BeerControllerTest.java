package com.ysf.spring6.reactive.controller;

import com.ysf.spring6.reactive.bootstrap.BootstrapData;
import com.ysf.spring6.reactive.dto.BeerDTO;
import com.ysf.spring6.reactive.mapper.BeerMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BeerMapper beerMapper;

    private static final String BEER_CONTROLLER_BASE_PATH = "/api/reactive/v1/beer/";

    private BeerDTO testBeerDTO;
    final int TEST_BEER_ID = 1;

    @BeforeEach
    void setUp() {
        this.testBeerDTO = this.beerMapper.toBeerDTO(BootstrapData.createTestBeerData().getFirst());
    }

    @Test
    @DisplayName("Get all beers")
    void testGetAllBeers() {
        this.webTestClient.get()
                .uri(BEER_CONTROLLER_BASE_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @DisplayName("Get beer by id")
    void testGetBeerById() {
        this.webTestClient.get()
                .uri(BEER_CONTROLLER_BASE_PATH + TEST_BEER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class).value((beerDTO) -> Assertions.assertEquals(TEST_BEER_ID, beerDTO.getId()));
    }

    @Test
    @DisplayName("Create new beer")
    void testCreateNewBeer() {
        this.webTestClient.post()
                .uri(BEER_CONTROLLER_BASE_PATH)
                .body(Mono.just(this.testBeerDTO), BeerDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(4);
    }

    @Test
    @DisplayName("Update existing beer")
    void testUpdateBeerById() {
        BeerDTO testUpdateBeerDTO = BeerDTO.builder()
                .beerName("Updated Test Beer")
                .beerStyle("Updated Test Beer Style")
                .build();

        this.webTestClient.put()
                .uri(BEER_CONTROLLER_BASE_PATH + TEST_BEER_ID)
                .body(Mono.just(testUpdateBeerDTO), BeerDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.beerName").isEqualTo(testUpdateBeerDTO.getBeerName())
                .jsonPath("$.beerStyle").isEqualTo(testUpdateBeerDTO.getBeerStyle())
                .jsonPath("$.upc").isEqualTo(this.testBeerDTO.getUpc())
                .jsonPath("$.quantityOnHand").isEqualTo(this.testBeerDTO.getQuantityOnHand());
    }

    // execute delete operation on DB in the end to avoid affecting other tests
    @Order(Integer.MAX_VALUE)
    @Test
    @DisplayName("Delete beer by id")
    void testDeleteBeerById() {
        this.webTestClient.delete()
                .uri(BEER_CONTROLLER_BASE_PATH + TEST_BEER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }
}