package com.ysf.spring6.rest.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysf.spring6.rest.mvc.model.Customer;
import com.ysf.spring6.rest.mvc.service.CustomerServiceImpl;
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

import java.util.Optional;
import java.util.UUID;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICustomerService customerServiceMock;

    @Captor
    private ArgumentCaptor<UUID> customerIdCaptor;
    @Captor
    ArgumentCaptor<Customer> customerCaptor;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerServiceImpl customerServiceImpl;

    private static final String CUSTOMER_CONTROLLER_BASE_URL = "/api/v1/customer/";

    @BeforeEach
    void setUp() {
        this.customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    @DisplayName("Get all customers")
    void listAllCustomers() throws Exception {
        Mockito.when(this.customerServiceMock.getAllCustomers())
                .thenReturn(this.customerServiceImpl.getAllCustomers());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_CONTROLLER_BASE_URL)
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
    @DisplayName("Get customer by ID")
    void getCustomerById() throws Exception {
        Customer customer = this.customerServiceImpl.getAllCustomers().getFirst();

        Mockito.when(this.customerServiceMock.getCustomerById(customer.getId()))
                .thenReturn(Optional.of(customer));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_CONTROLLER_BASE_URL + customer.getId())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").value(customer.getId().toString())
                );

        Mockito.verify(this.customerServiceMock, Mockito.atMostOnce())
                .getCustomerById(this.customerIdCaptor.capture());

        UUID capturedCustomerId = this.customerIdCaptor.getValue();
        Assertions.assertEquals(customer.getId(), capturedCustomerId);
    }

    @Test
    @DisplayName("Save new customer")
    void saveNewCustomer() throws Exception {
        Customer existingCustomer = this.customerServiceImpl.getAllCustomers().getFirst();
        Customer customerToBeCreated = Customer.builder()
                .name(existingCustomer.getName())
                .build();

        Mockito.when(this.customerServiceMock.saveNewCustomer(Mockito.any(Customer.class)))
                .thenReturn(existingCustomer);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CUSTOMER_CONTROLLER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(customerToBeCreated));

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

        Customer capturedCustomer = this.customerCaptor.getValue();
        Assertions.assertNull(capturedCustomer.getId());
        Assertions.assertNull(capturedCustomer.getVersion());
        Assertions.assertNull(capturedCustomer.getCreatedDate());
        Assertions.assertNull(capturedCustomer.getUpdateDate());
    }

    @Test
    @DisplayName("Update existing customer")
    void updateCustomer() throws Exception {
        Customer existingCustomer = this.customerServiceImpl.getAllCustomers().getFirst();
        Customer updatedCustomerData = Customer.builder()
                .name(existingCustomer.getName() + "- UPDATED")
                .build();

        Mockito.when(
                this.customerServiceMock.updateCustomerById(
                        Mockito.any(UUID.class),
                        Mockito.any(Customer.class)
                )
        ).thenReturn(Optional.of(updatedCustomerData));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CUSTOMER_CONTROLLER_BASE_URL + existingCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updatedCustomerData));

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.name").value(updatedCustomerData.getName())
                );

        Mockito.verify(this.customerServiceMock, Mockito.atMostOnce())
                .updateCustomerById(this.customerIdCaptor.capture(), Mockito.any(Customer.class));

        UUID capturedCustomerId = this.customerIdCaptor.getValue();
        Assertions.assertEquals(existingCustomer.getId(), capturedCustomerId);
    }

    @Test
    @DisplayName("Delete customer by id")
    void deleteCustomerById() throws Exception {
        Customer customerToDelete = this.customerServiceImpl.getAllCustomers().getFirst();

        Mockito.when(this.customerServiceMock.deleteCustomerById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(customerToDelete));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CUSTOMER_CONTROLLER_BASE_URL + customerToDelete.getId())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id").value(customerToDelete.getId().toString())
                );

        Mockito.verify(this.customerServiceMock, Mockito.atMostOnce())
                .deleteCustomerById(this.customerIdCaptor.capture());

        UUID capturedCustomerId = this.customerIdCaptor.getValue();
        Assertions.assertEquals(customerToDelete.getId(), capturedCustomerId);
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