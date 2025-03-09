package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICustomerService {

    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomerById(UUID uuid);

    CustomerDTO saveNewCustomer(CustomerDTO customerDTO);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO);

    Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customerDTO);

    Optional<CustomerDTO> deleteCustomerById(UUID customerId);
}