package com.ysf.spring6.rest.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysf.spring6.rest.mvc.model.Beer;
import com.ysf.spring6.rest.mvc.service.BeerServiceImpl;
import com.ysf.spring6.rest.mvc.service.IBeerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<UUID> beerIdCaptor;
    @Captor
    private ArgumentCaptor<Beer> beerCaptor;

    @MockitoBean
    private IBeerService beerServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private BeerServiceImpl beerServiceImpl;

    private static final String BEER_CONTROLLER_BASE_URL = "/api/v1/beer/";

    @BeforeEach
    void setUp() {
        this.beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    @DisplayName("Get all beers")
    void listBeers() throws Exception {
        Mockito.when(this.beerServiceMock.listBeers())
                .thenReturn(this.beerServiceImpl.listBeers());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BEER_CONTROLLER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.length()").value(3)
                );
    }

    @Test
    @DisplayName("Get beer by ID")
    void getBeerById() throws Exception {
        Beer beer = this.beerServiceImpl.listBeers().getFirst();

        Mockito.when(this.beerServiceMock.getBeerById(beer.getId()))
                .thenReturn(Optional.of(beer));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BEER_CONTROLLER_BASE_URL + beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").value(beer.getId().toString())
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .getBeerById(this.beerIdCaptor.capture());

        UUID capturedBeerId = this.beerIdCaptor.getValue();
        Assertions.assertEquals(beer.getId(), capturedBeerId);
    }

    @Test
    @DisplayName("Save new beer")
    void saveNewBeer() throws Exception {
        Beer existingBeer = this.beerServiceImpl.listBeers().getFirst();
        Beer beerToBeCreated = Beer.builder()
                .beerName(existingBeer.getBeerName())
                .beerStyle(existingBeer.getBeerStyle())
                .upc(existingBeer.getUpc())
                .quantityOnHand(existingBeer.getQuantityOnHand())
                .price(existingBeer.getPrice())
                .build();

        Mockito.when(this.beerServiceMock.saveNewBeer(Mockito.any(Beer.class)))
                .thenReturn(existingBeer);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BEER_CONTROLLER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(beerToBeCreated));

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").isNotEmpty(),
                        MockMvcResultMatchers.jsonPath("$.version").isNotEmpty(),
                        MockMvcResultMatchers.jsonPath("$.createdDate").isNotEmpty(),
                        MockMvcResultMatchers.jsonPath("$.updateDate").isNotEmpty()
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .saveNewBeer(this.beerCaptor.capture());

        Beer capturedBeer = this.beerCaptor.getValue();
        Assertions.assertNull(capturedBeer.getId());
        Assertions.assertNull(capturedBeer.getVersion());
        Assertions.assertNull(capturedBeer.getCreatedDate());
        Assertions.assertNull(capturedBeer.getUpdateDate());
    }

    @Test
    @DisplayName("Update existing beer")
    void updateBeer() throws Exception {
        Beer existingBeer = this.beerServiceImpl.listBeers().getFirst();
        Beer updatedBeerData = Beer.builder()
                .beerName(existingBeer.getBeerName() + " - UPDATED")
                .quantityOnHand(existingBeer.getQuantityOnHand() + 5)
                .price(existingBeer.getPrice().add(BigDecimal.valueOf(50)))
                .build();

        Mockito.when(
                this.beerServiceMock.updateBeerById(
                        Mockito.any(UUID.class),
                        Mockito.any(Beer.class)
                )
        ).thenReturn(Optional.of(updatedBeerData));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BEER_CONTROLLER_BASE_URL + existingBeer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updatedBeerData));

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.beerName").value(updatedBeerData.getBeerName()),
                        MockMvcResultMatchers.jsonPath("$.quantityOnHand").value(updatedBeerData.getQuantityOnHand()),
                        MockMvcResultMatchers.jsonPath("$.price").value(updatedBeerData.getPrice())
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .updateBeerById(this.beerIdCaptor.capture(), Mockito.any(Beer.class));

        UUID capturedBeerId = this.beerIdCaptor.getValue();
        Assertions.assertEquals(existingBeer.getId(), capturedBeerId);
    }

    @Test
    @DisplayName("Delete beer by id")
    void deleteBeerById() throws Exception {
        Beer beerToDelete = this.beerServiceImpl.listBeers().getFirst();

        Mockito.when(this.beerServiceMock.deleteBeerById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(beerToDelete));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BEER_CONTROLLER_BASE_URL + beerToDelete.getId())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").value(beerToDelete.getId().toString())
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .deleteBeerById(this.beerIdCaptor.capture());

        UUID capturedBeerId = this.beerIdCaptor.getValue();
        Assertions.assertEquals(beerToDelete.getId(), capturedBeerId);
    }

    @Test
    @DisplayName("Delete beer by non-existing id")
    void deleteBeerNotFoundIdThrowsException() throws Exception {
        UUID nonExistingId = UUID.randomUUID();

        Mockito.when(this.beerServiceMock.deleteBeerById(nonExistingId))
                .thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BEER_CONTROLLER_BASE_URL + nonExistingId)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .deleteBeerById(this.beerIdCaptor.capture());

        UUID capturedBeerId = this.beerIdCaptor.getValue();
        Assertions.assertEquals(nonExistingId, capturedBeerId);
    }
}