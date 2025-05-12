package com.ysf.spring6.reactive.service;

import com.ysf.spring6.reactive.dto.CustomerDTO;
import com.ysf.spring6.reactive.mapper.CustomerMapper;
import com.ysf.spring6.reactive.model.Customer;
import com.ysf.spring6.reactive.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDTO> getAllCustomers() {
        return this.customerRepository
                .findAll()
                .map(this.customerMapper::toCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(Integer customerId) {
        return this.customerRepository
                .findById(customerId)
                .map(this.customerMapper::toCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> createNewCustomer(CustomerDTO customerDTO) {
        Customer customer = this.customerMapper.toCustomer(customerDTO);
        return this.customerRepository
                .save(customer)
                .map(this.customerMapper::toCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> updateCustomerById(Integer customerId, CustomerDTO updateCustomerDTO) {
        return this.customerRepository
                .findById(customerId)
                .map(customer -> {
                    if (StringUtils.hasText(updateCustomerDTO.getName())) {
                        customer.setName(updateCustomerDTO.getName());
                    }
                    return customer;
                })
                .flatMap(this.customerRepository::save)
                .map(this.customerMapper::toCustomerDto);
    }

    @Override
    public Mono<Void> deleteCustomerById(Integer customerId) {
        return this.customerRepository.deleteById(customerId);
    }
}
