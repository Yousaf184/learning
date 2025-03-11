package com.ysf.spring6.rest.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysf.spring6.rest.mvc.dto.CustomerDTO;
import com.ysf.spring6.rest.mvc.service.ICustomerService;
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

import java.time.LocalDateTime;
import java.util.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICustomerService customerServiceMock;

    @Captor
    private ArgumentCaptor<UUID> customerIdCaptor;
    @Captor
    ArgumentCaptor<CustomerDTO> customerCaptor;

    @Autowired
    private ObjectMapper objectMapper;

    private List<CustomerDTO> testCustomerList;

    private static final String CUSTOMER_CONTROLLER_BASE_URL = "/api/v1/customer/";

    @BeforeEach
    void setUp() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.testCustomerList = new ArrayList<>();
        this.testCustomerList.add(customerDTO);
    }

    @Test
    @DisplayName("Get all customers")
    void listAllCustomers() throws Exception {
        Mockito.when(this.customerServiceMock.getAllCustomers())
                .thenReturn(this.testCustomerList);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_CONTROLLER_BASE_URL)
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
    @DisplayName("Get customer by ID")
    void getCustomerById() throws Exception {
        CustomerDTO customerDTO = this.testCustomerList.getFirst();

        Mockito.when(this.customerServiceMock.getCustomerById(customerDTO.getId()))
                .thenReturn(Optional.of(customerDTO));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_CONTROLLER_BASE_URL + customerDTO.getId())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").value(customerDTO.getId().toString())
                );

        Mockito.verify(this.customerServiceMock, Mockito.atMostOnce())
                .getCustomerById(this.customerIdCaptor.capture());

        UUID capturedCustomerId = this.customerIdCaptor.getValue();
        Assertions.assertEquals(customerDTO.getId(), capturedCustomerId);
    }

    @Test
    @DisplayName("Save new customer")
    void saveNewCustomer() throws Exception {
        CustomerDTO existingCustomerDTO = this.testCustomerList.getFirst();
        CustomerDTO customerDTOToBeCreated = CustomerDTO.builder()
                .name(existingCustomerDTO.getName())
                .build();

        Mockito.when(this.customerServiceMock.saveNewCustomer(Mockito.any(CustomerDTO.class)))
                .thenReturn(existingCustomerDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CUSTOMER_CONTROLLER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(customerDTOToBeCreated));

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

        Mockito.verify(this.customerServiceMock, Mockito.atMostOnce())
                .saveNewCustomer(this.customerCaptor.capture());

        CustomerDTO capturedCustomerDTO = this.customerCaptor.getValue();
        Assertions.assertNull(capturedCustomerDTO.getId());
        Assertions.assertNull(capturedCustomerDTO.getVersion());
        Assertions.assertNull(capturedCustomerDTO.getCreatedDate());
        Assertions.assertNull(capturedCustomerDTO.getUpdateDate());
    }

    @Test
    @DisplayName("Update existing customer")
    void updateCustomer() throws Exception {
        CustomerDTO existingCustomerDTO = this.testCustomerList.getFirst();
        CustomerDTO updatedCustomerDTOData = CustomerDTO.builder()
                .name(existingCustomerDTO.getName() + "- UPDATED")
                .build();

        Mockito.when(
                this.customerServiceMock.updateCustomerById(
                        Mockito.any(UUID.class),
                        Mockito.any(CustomerDTO.class)
                )
        ).thenReturn(Optional.of(updatedCustomerDTOData));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CUSTOMER_CONTROLLER_BASE_URL + existingCustomerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updatedCustomerDTOData));

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.name").value(updatedCustomerDTOData.getName())
                );

        Mockito.verify(this.customerServiceMock, Mockito.atMostOnce())
                .updateCustomerById(this.customerIdCaptor.capture(), Mockito.any(CustomerDTO.class));

        UUID capturedCustomerId = this.customerIdCaptor.getValue();
        Assertions.assertEquals(existingCustomerDTO.getId(), capturedCustomerId);
    }

    @Test
    @DisplayName("Delete customer by id")
    void deleteCustomerById() throws Exception {
        CustomerDTO customerDTOToDelete = this.testCustomerList.getFirst();

        Mockito.when(this.customerServiceMock.deleteCustomerById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(customerDTOToDelete));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CUSTOMER_CONTROLLER_BASE_URL + customerDTOToDelete.getId())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").value(customerDTOToDelete.getId().toString())
                );

        Mockito.verify(this.customerServiceMock, Mockito.atMostOnce())
                .deleteCustomerById(this.customerIdCaptor.capture());

        UUID capturedCustomerId = this.customerIdCaptor.getValue();
        Assertions.assertEquals(customerDTOToDelete.getId(), capturedCustomerId);
    }

    @Test
    @DisplayName("Delete customer by non-existing id")
    void deleteCustomerNotFoundIdThrowsException() throws Exception {
        UUID nonExistingId = UUID.randomUUID();

        Mockito.when(this.customerServiceMock.deleteCustomerById(nonExistingId))
                .thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CUSTOMER_CONTROLLER_BASE_URL + nonExistingId)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(this.customerServiceMock, Mockito.atMostOnce())
                .deleteCustomerById(this.customerIdCaptor.capture());

        UUID capturedCustomerId = this.customerIdCaptor.getValue();
        Assertions.assertEquals(nonExistingId, capturedCustomerId);
    }
}