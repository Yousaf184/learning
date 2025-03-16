package com.ysf.spring6.rest.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.ysf.spring6.rest.mvc.bootstrap.BootstrapTestData;
import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.dto.BeerDTO;
import com.ysf.spring6.rest.mvc.entity.Beer;
import com.ysf.spring6.rest.mvc.exceptions.NotFoundException;
import com.ysf.spring6.rest.mvc.mapper.BeerMapper;
import com.ysf.spring6.rest.mvc.repository.BeerRepository;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(BootstrapTestData.class)
@Transactional
@Rollback
class BeerControllerIT {

    private final BeerController beerController;
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private final TestRestTemplate testRestTemplate;
    private final ObjectMapper objectMapper;

    private static final String BEER_CONTROLLER_BASE_URL = "/api/v1/beer/";

    @Autowired
    public BeerControllerIT(
            BeerController beerController,
            BeerRepository beerRepository,
            BeerMapper beerMapper,
            TestRestTemplate testRestTemplate,
            ObjectMapper objectMapper
    ) {
        this.beerController = beerController;
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
        this.testRestTemplate = testRestTemplate;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("Get all beers")
    void getAllBeers() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(BEER_CONTROLLER_BASE_URL, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Integer defaultPageSize = 5;
        Integer defaultPageNum = 1;

        Object parsedJson = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
        Assertions.assertEquals(defaultPageSize, JsonPath.read(parsedJson, "$.data.length()"));
        Assertions.assertEquals(defaultPageSize, JsonPath.read(parsedJson, "$.currentRecordCount"));
        Assertions.assertEquals(defaultPageNum, JsonPath.read(parsedJson, "$.currentPageNumber"));
        Assertions.assertEquals(defaultPageSize, JsonPath.read(parsedJson, "$.pageSize"));
        Assertions.assertNotNull(JsonPath.read(parsedJson, "$.totalPageCount"));
        Assertions.assertNotNull(JsonPath.read(parsedJson, "$.totalRowCount"));
        Assertions.assertEquals("beerName", JsonPath.read(parsedJson, "$.sortKey"));
        Assertions.assertEquals("asc", JsonPath.read(parsedJson, "$.sortOrder"));
    }

    @Test
    @DisplayName("Get all beers by name")
    void getAllBeersMatchingNameIgnoreCase() {
        final String beerNameToSearch = "Dusty";

        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("beerName", beerNameToSearch)
                .toUriString();
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(requestUrl, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Object parsedJson = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
        List<BeerDTO> beerListMatchByName = JsonPath.read(parsedJson,"$.data[?(@.beerName =~ /.*"+beerNameToSearch+".*/i)]");
        int currentRecordCount = JsonPath.read(parsedJson, "$.currentRecordCount");

        Assertions.assertEquals(currentRecordCount, beerListMatchByName.size());
        Assertions.assertEquals(Integer.valueOf(1), JsonPath.read(parsedJson, "$.totalRowCount"));
    }

    @Test
    @DisplayName("Get all beers by beer style")
    void getAllBeersBeerStyle() {
        final BeerStyle beerStyleToSearch = BeerStyle.IPA;
        final Integer REQUESTED_PAGE_SIZE = 10;

        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("beerStyle", beerStyleToSearch)
                .queryParam("pageSize", REQUESTED_PAGE_SIZE)
                .toUriString();
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(requestUrl, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        final Integer IPA_STYLE_BEER_COUNT = 548;
        Object parsedJson = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
        Assertions.assertEquals(REQUESTED_PAGE_SIZE, JsonPath.read(parsedJson, "$.currentRecordCount"));
        Assertions.assertEquals(REQUESTED_PAGE_SIZE, JsonPath.read(parsedJson, "$.pageSize"));
        Assertions.assertEquals(IPA_STYLE_BEER_COUNT, JsonPath.read(parsedJson, "$.totalRowCount"));
    }

    @Test
    @DisplayName("Get all beers by name and beer style")
    void getAllBeersMatchingNameIgnoreCaseAndBeerStyle() {
        final String beerNameToSearch = "galaxy";
        final BeerStyle beerStyleToSearch = BeerStyle.IPA;

        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("beerStyle", beerStyleToSearch)
                .queryParam("beerName", beerNameToSearch)
                .toUriString();
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(requestUrl, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        final Integer IPA_STYLE_GALAXY_BEER_COUNT = 4;
        Object parsedJson = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
        Assertions.assertEquals(IPA_STYLE_GALAXY_BEER_COUNT, JsonPath.read(parsedJson, "$.currentRecordCount"));
        Assertions.assertEquals(IPA_STYLE_GALAXY_BEER_COUNT, JsonPath.read(parsedJson, "$.totalRowCount"));
    }

    @Test
    @DisplayName("Default max page size")
    void testDefaultMaxPageSize() {
        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("pageSize", 100)
                .toUriString();
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(requestUrl, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        final Integer DEFAULT_MAX_PAGE_SIZE = 50;
        Assertions.assertEquals(DEFAULT_MAX_PAGE_SIZE, JsonPath.read(response.getBody(), "$.pageSize"));
    }

    @Test
    @DisplayName("Sort by quantity (ASC)")
    void testSortByQuantityAscendingOrder() {
        final String SORT_FIELD = "quantityOnHand";
        final String SORT_ORDER = "asc";

        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("sortByField", SORT_FIELD)
                .queryParam("sortOrder", SORT_ORDER)
                .toUriString();
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(requestUrl, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Object parsedJson = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
        Assertions.assertEquals(SORT_FIELD, JsonPath.read(parsedJson, "$.sortKey"));
        Assertions.assertEquals(SORT_ORDER, JsonPath.read(parsedJson, "$.sortOrder"));
        final Integer TEST_BEERS_MIN_QUANTITY = 1;
        Assertions.assertEquals(TEST_BEERS_MIN_QUANTITY, JsonPath.read(parsedJson, "$.data[0].quantityOnHand"));
    }

    @Test
    @DisplayName("Sort by price (DESC)")
    void testSortByPriceDescendingOrder() {
        final String SORT_FIELD = "price";
        final String SORT_ORDER = "desc";

        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("sortByField", SORT_FIELD)
                .queryParam("sortOrder", SORT_ORDER)
                .toUriString();
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(requestUrl, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Object parsedJson = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
        Assertions.assertEquals(SORT_FIELD, JsonPath.read(parsedJson, "$.sortKey"));
        Assertions.assertEquals(SORT_ORDER, JsonPath.read(parsedJson, "$.sortOrder"));
        final Double TEST_BEERS_MAX_PRICE = 13.99;
        Assertions.assertEquals(TEST_BEERS_MAX_PRICE, JsonPath.read(parsedJson, "$.data[0].price"));
    }

    @Test
    @DisplayName("Get beer by ID")
    void getBeerById() throws JsonProcessingException, JSONException {
        Beer existingBeer = this.beerRepository.findAll().getFirst();

        String requestUrl = BEER_CONTROLLER_BASE_URL + existingBeer.getId();
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(requestUrl, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        String expectedJson = this.objectMapper.writeValueAsString(existingBeer);
        JSONAssert.assertEquals(expectedJson, response.getBody(), false);
    }

    @Test
    @DisplayName("Get beer by non-existing ID throws")
    void getBeerByNonExistingIdThrowsException() {
        UUID nonExistingBeerId = UUID.randomUUID();
        Assertions.assertThrows(NotFoundException.class, () -> this.beerController.getBeerById(nonExistingBeerId));

        String requestUrl = BEER_CONTROLLER_BASE_URL + UUID.randomUUID();
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(requestUrl, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("Save new beer")
    void saveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("Test beer name")
                .beerStyle(BeerStyle.GOSE)
                .upc("test upc")
                .quantityOnHand(10)
                .price(BigDecimal.valueOf(20.5))
                .build();

        ResponseEntity<BeerDTO> responseEntity = this.beerController.saveNewBeer(beerDTO);
        BeerDTO savedBeer = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertNotNull(savedBeer);
        Assertions.assertNotNull(savedBeer.getId());
        Assertions.assertNotNull(savedBeer.getCreatedDate());
        Assertions.assertNotNull(savedBeer.getUpdateDate());
    }

    @Test
    @DisplayName("Update beer by ID")
    void updateBeerById() {
        Beer existingBeer = this.beerRepository.findAll().getFirst();

        BeerDTO beerDTO = this.beerMapper.beerToBeerDTO(existingBeer);
        beerDTO.setBeerName("TEST NAME UPDATE");
        beerDTO.setQuantityOnHand(25);
        beerDTO.setPrice(BigDecimal.valueOf(52.20));
        beerDTO.setCreatedDate(LocalDateTime.now()); // shouldn't be updated

        ResponseEntity<BeerDTO> responseEntity = this.beerController.updateBeerById(beerDTO.getId(), beerDTO);
        BeerDTO updatedBeer = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Assertions.assertNotNull(updatedBeer);

        Assertions.assertEquals(existingBeer.getId(), updatedBeer.getId());
        Assertions.assertEquals(beerDTO.getBeerName(), updatedBeer.getBeerName());
        Assertions.assertEquals(beerDTO.getQuantityOnHand(), updatedBeer.getQuantityOnHand());
        Assertions.assertEquals(beerDTO.getPrice(), updatedBeer.getPrice());

        Assertions.assertNotEquals(beerDTO.getCreatedDate(), updatedBeer.getCreatedDate());
        Assertions.assertNotEquals(beerDTO.getUpdateDate(), updatedBeer.getUpdateDate());
    }

    @Test
    @DisplayName("Update beer by non-existing ID throws")
    void updateBeerByNotExistingIdThrowsException() {
        UUID nonExistingBeerId = UUID.randomUUID();
        Assertions.assertThrows(NotFoundException.class, () -> this.beerController.updateBeerById(nonExistingBeerId, null));
    }

    @Test
    @DisplayName("Delete beer by ID")
    void deleteBeerById() {
        Beer existingBeer = this.beerRepository.findAll().getFirst();

        ResponseEntity<BeerDTO> responseEntity = this.beerController.deleteBeerById(existingBeer.getId());
        BeerDTO deletedBeer = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(deletedBeer);

        Optional<Beer> beerOptional = this.beerRepository.findById(existingBeer.getId());
        Assertions.assertTrue(beerOptional.isEmpty());
    }

    @Test
    @DisplayName("Delete beer by non-existing ID throws")
    void deleteBeerByNotExistingIdThrowsException() {
        UUID nonExistingBeerId = UUID.randomUUID();
        Assertions.assertThrows(NotFoundException.class, () -> this.beerController.deleteBeerById(nonExistingBeerId));
    }
}