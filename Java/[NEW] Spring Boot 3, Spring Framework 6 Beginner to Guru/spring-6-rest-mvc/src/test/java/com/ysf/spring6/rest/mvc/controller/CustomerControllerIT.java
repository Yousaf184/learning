package com.ysf.spring6.rest.mvc.controller;

import com.ysf.spring6.rest.mvc.bootstrap.BootstrapTestData;
import com.ysf.spring6.rest.mvc.dto.CustomerDTO;
import com.ysf.spring6.rest.mvc.entity.Customer;
import com.ysf.spring6.rest.mvc.exceptions.NotFoundException;
import com.ysf.spring6.rest.mvc.mapper.CustomerMapper;
import com.ysf.spring6.rest.mvc.repository.CustomerRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Import(BootstrapTestData.class)
@Transactional
@Rollback
class CustomerControllerIT {

    private final CustomerController customerController;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerControllerIT(
        CustomerController customerController,
        CustomerRepository customerRepository,
        CustomerMapper customerMapper
    ) {
        this.customerController = customerController;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Test
    @DisplayName("Get all customer")
    void getAllCustomer() {
        ResponseEntity<List<CustomerDTO>> responseEntity = this.customerController.listAllCustomers();
        List<CustomerDTO> customerList = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(customerList);
        Assertions.assertEquals(3, customerList.size());
    }

    @Test
    @DisplayName("Empty customers list")
    void emptyCustomerTableReturnsEmptyList() {
        this.customerRepository.deleteAll();

        ResponseEntity<List<CustomerDTO>> responseEntity = this.customerController.listAllCustomers();
        List<CustomerDTO> customerList = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(customerList);
        Assertions.assertEquals(0, customerList.size());
    }

    @Test
    @DisplayName("Get customer by ID")
    void getCustomerById() {
        Customer existingCustomer = this.customerRepository.findAll().getFirst();

        ResponseEntity<CustomerDTO> responseEntity = this.customerController.getCustomerById(existingCustomer.getId());
        CustomerDTO returnedCustomer = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(returnedCustomer);
        Assertions.assertEquals(existingCustomer.getId(), returnedCustomer.getId());
    }

    @Test
    @DisplayName("Get customer by non-existing ID throws")
    void getCustomerByNonExistingIdThrowsException() {
        UUID nonExistingCustomerId = UUID.randomUUID();
        Assertions.assertThrows(NotFoundException.class, () -> this.customerController.getCustomerById(nonExistingCustomerId));
    }

    @Test
    @DisplayName("Save new customer")
    void saveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("test customer name")
                .build();

        ResponseEntity<CustomerDTO> responseEntity = this.customerController.saveNewCustomer(customerDTO);
        CustomerDTO savedCustomer = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        Assertions.assertNotNull(savedCustomer);
        Assertions.assertNotNull(savedCustomer.getId());
        Assertions.assertNotNull(savedCustomer.getCreatedDate());
        Assertions.assertNotNull(savedCustomer.getUpdateDate());
    }

    @Test
    @DisplayName("Update customer by ID")
    void updateCustomerById() {
        Customer existingCustomer = this.customerRepository.findAll().getFirst();

        CustomerDTO customerDTO = this.customerMapper.customerToCustomerDTO(existingCustomer);
        customerDTO.setName("TEST NAME UPDATE");
        customerDTO.setCreatedDate(LocalDateTime.now()); // shouldn't get updated

        ResponseEntity<CustomerDTO> responseEntity = this.customerController.updateCustomerById(customerDTO.getId(), customerDTO);
        CustomerDTO updatedCustomer = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Assertions.assertNotNull(updatedCustomer);

        Assertions.assertEquals(existingCustomer.getId(), updatedCustomer.getId());
        Assertions.assertEquals(customerDTO.getName(), updatedCustomer.getName());

        Assertions.assertNotEquals(customerDTO.getCreatedDate(), updatedCustomer.getCreatedDate());
        Assertions.assertNotEquals(customerDTO.getUpdateDate(), updatedCustomer.getUpdateDate());
    }

    @Test
    @DisplayName("Update customer by non-existing ID throws")
    void updateCustomerByNonExistingIdThrowsException() {
        UUID nonExistingCustomerId = UUID.randomUUID();
        Assertions.assertThrows(NotFoundException.class, () -> this.customerController.updateCustomerById(nonExistingCustomerId, null));
    }

    @Test
    @DisplayName("Delete customer by ID")
    void deleteCustomerById() {
        Customer existingCustomer = this.customerRepository.findAll().getFirst();

        ResponseEntity<CustomerDTO> responseEntity = this.customerController.deleteCustomerById(existingCustomer.getId());
        CustomerDTO deletedCustomer = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(deletedCustomer);

        Optional<Customer> customerOptional = this.customerRepository.findById(existingCustomer.getId());
        Assertions.assertTrue(customerOptional.isEmpty());
    }

    @Test
    @DisplayName("Delete customer by non-existing ID throws")
    void deleteCustomerByNonExistingIdThrowsException() {
        UUID nonExistingCustomerId = UUID.randomUUID();
        Assertions.assertThrows(NotFoundException.class, () -> this.customerController.deleteCustomerById(nonExistingCustomerId));
    }
}