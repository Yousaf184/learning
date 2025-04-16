package com.ysf.spring6.reactive.controller;

import com.ysf.spring6.reactive.bootstrap.BootstrapData;
import com.ysf.spring6.reactive.dto.CustomerDTO;
import com.ysf.spring6.reactive.mapper.CustomerMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerMapper customerMapper;

    private static final String CUSTOMER_CONTROLLER_BASE_PATH = "/api/reactive/v1/customer/";

    private CustomerDTO testCustomerDTO;
    final int TEST_CUSTOMER_ID = 1;

    @BeforeEach
    void setUp() {
        this.testCustomerDTO = this.customerMapper.toCustomerDto(BootstrapData.createTestCustomerData().getFirst());
    }

    @Test
    @DisplayName("Get all customers")
    void testGetAllCustomers() {
        this.webTestClient.get()
                .uri(CUSTOMER_CONTROLLER_BASE_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @DisplayName("Get customer by id")
    void testGetCustomerById() {
        this.webTestClient.get()
                .uri(CUSTOMER_CONTROLLER_BASE_PATH + TEST_CUSTOMER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class).value((custDTO) -> Assertions.assertEquals(TEST_CUSTOMER_ID, custDTO.getId()));
    }

    @Test
    @DisplayName("Create new customer")
    void testCreateNewCustomer() {
        this.webTestClient.post()
                .uri(CUSTOMER_CONTROLLER_BASE_PATH)
                .body(Mono.just(this.testCustomerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(4);
    }

    @Test
    @DisplayName("Update existing customer")
    void testUpdateCustomerById() {
        CustomerDTO testUpdateCustomerDTO = CustomerDTO.builder()
                .name("Test Customer")
                .build();

        this.webTestClient.put()
                .uri(CUSTOMER_CONTROLLER_BASE_PATH + TEST_CUSTOMER_ID)
                .body(Mono.just(testUpdateCustomerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(testUpdateCustomerDTO.getName());
    }

    // execute delete operation on DB in the end to avoid affecting other tests
    @Order(Integer.MAX_VALUE)
    @Test
    @DisplayName("Delete customer by id")
    void testDeleteCustomerById() {
        this.webTestClient.delete()
                .uri(CUSTOMER_CONTROLLER_BASE_PATH + TEST_CUSTOMER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }
}