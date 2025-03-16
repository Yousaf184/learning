package com.ysf.spring6.rest.mvc.controller;

import com.ysf.spring6.rest.mvc.bootstrap.BootstrapTestData;
import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.dto.BeerDTO;
import com.ysf.spring6.rest.mvc.entity.Beer;
import com.ysf.spring6.rest.mvc.exceptions.NotFoundException;
import com.ysf.spring6.rest.mvc.mapper.BeerMapper;
import com.ysf.spring6.rest.mvc.repository.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Import(BootstrapTestData.class)
@Transactional
@Rollback
class BeerControllerIT {

    private final BeerController beerController;
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Autowired
    public BeerControllerIT(
            BeerController beerController,
            BeerRepository beerRepository,
            BeerMapper beerMapper
    ) {
        this.beerController = beerController;
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Test
    @DisplayName("Get all beers")
    void getAllBeers() {
        ResponseEntity<List<BeerDTO>> responseEntity = this.beerController.listBeers(null, null);
        List<BeerDTO> beerList = responseEntity.getBody();

        final int TEST_BEER_DATA_COUNT = 2413;
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(beerList);
        Assertions.assertEquals(TEST_BEER_DATA_COUNT, beerList.size());
    }

    @Test
    @DisplayName("Empty beers list")
    void emptyBeerTableReturnsEmptyList() {
        this.beerRepository.deleteAll();

        ResponseEntity<List<BeerDTO>> responseEntity = this.beerController.listBeers(null, null);
        List<BeerDTO> beerList = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(beerList);
        Assertions.assertEquals(0, beerList.size());
    }

    @Test
    @DisplayName("Get all beers by name")
    void getAllBeersMatchingNameIgnoreCase() {
        final String beerNameToSearch = "galaxy";
        ResponseEntity<List<BeerDTO>> responseEntity = this.beerController.listBeers(beerNameToSearch, null);
        List<BeerDTO> beerList = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final int GALAXY_BEER_COUNT = 5;
        Assertions.assertNotNull(beerList);
        Assertions.assertEquals(GALAXY_BEER_COUNT, beerList.size());
    }

    @Test
    @DisplayName("Get all beers by beer style")
    void getAllBeersBeerStyle() {
        final BeerStyle beerStyleToSearch = BeerStyle.IPA;
        ResponseEntity<List<BeerDTO>> responseEntity = this.beerController.listBeers(null, beerStyleToSearch);
        List<BeerDTO> beerList = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final int IPA_STYLE_BEER_COUNT = 548;
        Assertions.assertNotNull(beerList);
        Assertions.assertEquals(IPA_STYLE_BEER_COUNT, beerList.size());
    }

    @Test
    @DisplayName("Get all beers by name and beer style")
    void getAllBeersMatchingNameIgnoreCaseAndBeerStyle() {
        final String beerNameToSearch = "galaxy";
        final BeerStyle beerStyleToSearch = BeerStyle.IPA;
        ResponseEntity<List<BeerDTO>> responseEntity = this.beerController.listBeers(beerNameToSearch, beerStyleToSearch);
        List<BeerDTO> beerList = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final int IPA_STYLE_GALAXY_BEER_COUNT = 4;
        Assertions.assertNotNull(beerList);
        Assertions.assertEquals(IPA_STYLE_GALAXY_BEER_COUNT, beerList.size());
    }

    @Test
    @DisplayName("Get beer by ID")
    void getBeerById() {
        Beer existingBeer = this.beerRepository.findAll().getFirst();

        ResponseEntity<BeerDTO> responseEntity = this.beerController.getBeerById(existingBeer.getId());
        BeerDTO returnedBeer = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(returnedBeer);
        Assertions.assertEquals(existingBeer.getId(), returnedBeer.getId());
    }

    @Test
    @DisplayName("Get beer by non-existing ID throws")
    void getBeerByNonExistingIdThrowsException() {
        UUID nonExistingBeerId = UUID.randomUUID();
        Assertions.assertThrows(NotFoundException.class, () -> this.beerController.getBeerById(nonExistingBeerId));
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