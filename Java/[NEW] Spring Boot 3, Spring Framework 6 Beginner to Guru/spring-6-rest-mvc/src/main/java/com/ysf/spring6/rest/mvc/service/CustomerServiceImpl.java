package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.dto.CustomerDTO;
import com.ysf.spring6.rest.mvc.entity.Customer;
import com.ysf.spring6.rest.mvc.mapper.CustomerMapper;
import com.ysf.spring6.rest.mvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public List<CustomerDTO> getAllCustomers() {
        return this.customerRepository.findAll()
                .stream()
                .map(this.customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);
        Customer customer = customerOptional.orElse(null);

        CustomerDTO customerDTO = this.customerMapper.customerToCustomerDTO(customer);

        return Optional.ofNullable(customerDTO);
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        if (customerDTO.getId() != null) {
            customerDTO.setId(null);
        }

        if (customerDTO.getVersion() != null) {
            customerDTO.setVersion(null);
        }

        Customer customerToSave = this.customerMapper.customerDTOToCustomer(customerDTO);
        Customer savedCustomer = this.customerRepository.save(customerToSave);

        return this.customerMapper.customerToCustomerDTO(savedCustomer);
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);

        if (customerOptional.isEmpty() || !customerId.equals(customerDTO.getId())) {
            return Optional.empty();
        }

        Customer customerToUpdate = customerOptional.orElse(null);

        if (StringUtils.hasText(customerDTO.getName())) {
            customerToUpdate.setName(customerDTO.getName());
        }

        Customer updatedCustomer = this.customerRepository.save(customerToUpdate);

        CustomerDTO updatedCustomerDTO = this.customerMapper.customerToCustomerDTO(updatedCustomer);
        return Optional.of(updatedCustomerDTO);
    }

    @Override
    public Optional<CustomerDTO> deleteCustomerById(UUID customerId) {
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);

        if (customerOptional.isEmpty()) {
            return Optional.empty();
        }

        this.customerRepository.deleteById(customerId);

        Customer deletedCustomer = customerOptional.orElse(null);
        CustomerDTO deletedCustomerDTO = this.customerMapper.customerToCustomerDTO(deletedCustomer);

        return Optional.ofNullable(deletedCustomerDTO);
    }
}