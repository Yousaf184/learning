package com.ysf.spring6.reactive.service;

import com.ysf.spring6.reactive.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {

    Flux<CustomerDTO> getAllCustomers();

    Mono<CustomerDTO> getCustomerById(Integer customerId);

    Mono<CustomerDTO> createNewCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> updateCustomerById(Integer customerId, CustomerDTO customerDTO);

    Mono<Void> deleteCustomerById(Integer customerId);
}
