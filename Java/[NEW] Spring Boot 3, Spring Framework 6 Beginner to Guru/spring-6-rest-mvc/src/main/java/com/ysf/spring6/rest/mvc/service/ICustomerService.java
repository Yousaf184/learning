package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICustomerService {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(UUID uuid);

    Customer saveNewCustomer(Customer customer);

    Optional<Customer> updateCustomerById(UUID customerId, Customer customer);

    Optional<Customer> patchCustomerById(UUID customerId, Customer customer);

    Optional<Customer> deleteCustomerById(UUID customerId);
}