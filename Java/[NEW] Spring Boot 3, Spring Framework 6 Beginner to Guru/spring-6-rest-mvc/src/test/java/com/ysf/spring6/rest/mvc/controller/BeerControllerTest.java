package com.ysf.spring6.rest.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.dto.BeerDTO;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@WebMvcTest(BeerController.class)
@ActiveProfiles("test")
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<UUID> beerIdCaptor;
    @Captor
    private ArgumentCaptor<BeerDTO> beerCaptor;
    @Captor
    private ArgumentCaptor<String> beerNameCaptor;
    @Captor
    private ArgumentCaptor<BeerStyle> beerStyleCaptor;

    @MockitoBean
    private IBeerService beerServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private List<BeerDTO> testBeerList;

    private static final String BEER_CONTROLLER_BASE_URL = "/api/v1/beer/";

    @BeforeEach
    void setUp() {
        BeerDTO beerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.testBeerList = new ArrayList<>();
        this.testBeerList.add(beerDTO);
    }

    @Test
    @DisplayName("Get all beers")
    void listBeers() throws Exception {
        Mockito.when(this.beerServiceMock.listBeers(null, null))
                .thenReturn(this.testBeerList);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BEER_CONTROLLER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.length()").value(1)
                );
    }

    @Test
    @DisplayName("Get all beers by name")
    void getAllBeersMatchingNameIgnoreCase() throws Exception {
        Mockito.when(this.beerServiceMock.listBeers(Mockito.anyString(), Mockito.isNull()))
                .thenReturn(this.testBeerList);

        final String beerNameToSearch = "galaxy";
        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("beerName", beerNameToSearch)
                .toUriString();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(requestUrl)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .listBeers(this.beerNameCaptor.capture(), Mockito.isNull());

        String capturedBeerName = this.beerNameCaptor.getValue();
        Assertions.assertEquals(beerNameToSearch, capturedBeerName);
    }

    @Test
    @DisplayName("Get all beers by beer style")
    void getAllBeersBeerStyle() throws Exception {
        Mockito.when(this.beerServiceMock.listBeers(Mockito.isNull(), Mockito.any(BeerStyle.class)))
                .thenReturn(this.testBeerList);

        final String beerStyleToSearch = BeerStyle.PALE_ALE.toString();
        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("beerStyle", beerStyleToSearch)
                .toUriString();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(requestUrl)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .listBeers(Mockito.isNull(), this.beerStyleCaptor.capture());

        BeerStyle capturedBeerStyle = this.beerStyleCaptor.getValue();
        Assertions.assertEquals(beerStyleToSearch, capturedBeerStyle.toString());
    }

    @Test
    @DisplayName("Get all beers by name and beer style")
    void getAllBeersMatchingNameIgnoreCaseAndBeerStyle() throws Exception {
        Mockito.when(this.beerServiceMock.listBeers(Mockito.anyString(), Mockito.any(BeerStyle.class)))
                .thenReturn(this.testBeerList);

        final String beerNameToSearch = "galaxy";
        final String beerStyleToSearch = BeerStyle.PALE_ALE.toString();
        String requestUrl = UriComponentsBuilder.fromUriString(BEER_CONTROLLER_BASE_URL)
                .queryParam("beerName", beerNameToSearch)
                .queryParam("beerStyle", beerStyleToSearch)
                .toUriString();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(requestUrl)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .listBeers(this.beerNameCaptor.capture(), this.beerStyleCaptor.capture());

        String capturedBeerName = this.beerNameCaptor.getValue();
        BeerStyle capturedBeerStyle = this.beerStyleCaptor.getValue();
        Assertions.assertEquals(beerNameToSearch, capturedBeerName);
        Assertions.assertEquals(beerStyleToSearch, capturedBeerStyle.toString());
    }

    @Test
    @DisplayName("Get beer by ID")
    void getBeerById() throws Exception {
        BeerDTO beerDTO = this.testBeerList.getFirst();

        Mockito.when(this.beerServiceMock.getBeerById(beerDTO.getId()))
                .thenReturn(Optional.of(beerDTO));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BEER_CONTROLLER_BASE_URL + beerDTO.getId().toString())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").value(beerDTO.getId().toString())
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .getBeerById(this.beerIdCaptor.capture());

        UUID capturedBeerId = this.beerIdCaptor.getValue();
        Assertions.assertEquals(beerDTO.getId(), capturedBeerId);
    }

    @Test
    @DisplayName("Save new beer")
    void saveNewBeer() throws Exception {
        BeerDTO existingBeerDTO = this.testBeerList.getFirst();
        BeerDTO beerDTOToBeCreated = BeerDTO.builder()
                .beerName(existingBeerDTO.getBeerName())
                .beerStyle(existingBeerDTO.getBeerStyle())
                .upc(existingBeerDTO.getUpc())
                .quantityOnHand(existingBeerDTO.getQuantityOnHand())
                .price(existingBeerDTO.getPrice())
                .build();

        Mockito.when(this.beerServiceMock.saveNewBeer(Mockito.any(BeerDTO.class)))
                .thenReturn(existingBeerDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BEER_CONTROLLER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(beerDTOToBeCreated));

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

        BeerDTO capturedBeerDTO = this.beerCaptor.getValue();
        Assertions.assertNull(capturedBeerDTO.getId());
        Assertions.assertNull(capturedBeerDTO.getVersion());
        Assertions.assertNull(capturedBeerDTO.getCreatedDate());
        Assertions.assertNull(capturedBeerDTO.getUpdateDate());
    }

    @Test
    @DisplayName("Validate beer fields before saving")
    void validateBeerDataBeforeSave() throws Exception {
        BeerDTO emptyBeerDTO = BeerDTO.builder().build();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BEER_CONTROLLER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(emptyBeerDTO));

        final int ERROR_FIELD_COUNT = 5;
        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status").value("error"),
                        MockMvcResultMatchers.jsonPath("$.errors").exists(),
                        MockMvcResultMatchers.jsonPath("$.errors.length()").value(ERROR_FIELD_COUNT),
                        MockMvcResultMatchers.jsonPath("$.errors.beerName").exists(),
                        MockMvcResultMatchers.jsonPath("$.errors.beerStyle").exists(),
                        MockMvcResultMatchers.jsonPath("$.errors.upc").exists(),
                        MockMvcResultMatchers.jsonPath("$.errors.quantityOnHand").exists(),
                        MockMvcResultMatchers.jsonPath("$.errors.price").exists()
                );

        Mockito.verify(this.beerServiceMock, Mockito.never())
                .saveNewBeer(Mockito.any());
    }

    @Test
    @DisplayName("Validate max size constraints on beer fields")
    void validateMaxSizeConstraintsOnBeerFields() throws Exception {
        BeerDTO testBeerDTO = this.testBeerList.getFirst();

        testBeerDTO.setBeerName("test".repeat(300));
        testBeerDTO.setUpc("test".repeat(255));
        testBeerDTO.setQuantityOnHand(2_000_000_000); // 2 billion

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BEER_CONTROLLER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(testBeerDTO));

        final int ERROR_FIELD_COUNT = 3;
        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status").value("error"),
                        MockMvcResultMatchers.jsonPath("$.errors").exists(),
                        MockMvcResultMatchers.jsonPath("$.errors.length()").value(ERROR_FIELD_COUNT),
                        MockMvcResultMatchers.jsonPath("$.errors.beerName").exists(),
                        MockMvcResultMatchers.jsonPath("$.errors.upc").exists(),
                        MockMvcResultMatchers.jsonPath("$.errors.quantityOnHand").exists()
                );

        Mockito.verify(this.beerServiceMock, Mockito.never())
                .saveNewBeer(Mockito.any());
    }

    @Test
    @DisplayName("Update existing beer")
    void updateBeer() throws Exception {
        BeerDTO existingBeerDTO = this.testBeerList.getFirst();
        BeerDTO updatedBeerDTOData = BeerDTO.builder()
                .beerName(existingBeerDTO.getBeerName() + " - UPDATED")
                .quantityOnHand(existingBeerDTO.getQuantityOnHand() + 5)
                .price(existingBeerDTO.getPrice().add(BigDecimal.valueOf(50)))
                .build();

        Mockito.when(
                this.beerServiceMock.updateBeerById(
                        Mockito.any(UUID.class),
                        Mockito.any(BeerDTO.class)
                )
        ).thenReturn(Optional.of(updatedBeerDTOData));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BEER_CONTROLLER_BASE_URL + existingBeerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updatedBeerDTOData));

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.beerName").value(updatedBeerDTOData.getBeerName()),
                        MockMvcResultMatchers.jsonPath("$.quantityOnHand").value(updatedBeerDTOData.getQuantityOnHand()),
                        MockMvcResultMatchers.jsonPath("$.price").value(updatedBeerDTOData.getPrice())
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .updateBeerById(this.beerIdCaptor.capture(), Mockito.any(BeerDTO.class));

        UUID capturedBeerId = this.beerIdCaptor.getValue();
        Assertions.assertEquals(existingBeerDTO.getId(), capturedBeerId);
    }

    @Test
    @DisplayName("Delete beer by id")
    void deleteBeerById() throws Exception {
        BeerDTO beerDTOToDelete = this.testBeerList.getFirst();

        Mockito.when(this.beerServiceMock.deleteBeerById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(beerDTOToDelete));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BEER_CONTROLLER_BASE_URL + beerDTOToDelete.getId())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").value(beerDTOToDelete.getId().toString())
                );

        Mockito.verify(this.beerServiceMock, Mockito.atMostOnce())
                .deleteBeerById(this.beerIdCaptor.capture());

        UUID capturedBeerId = this.beerIdCaptor.getValue();
        Assertions.assertEquals(beerDTOToDelete.getId(), capturedBeerId);
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