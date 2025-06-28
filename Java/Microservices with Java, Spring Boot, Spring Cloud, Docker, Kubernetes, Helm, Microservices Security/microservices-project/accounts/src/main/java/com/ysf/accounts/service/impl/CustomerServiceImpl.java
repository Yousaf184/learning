package com.ysf.accounts.service.impl;

import com.ysf.accounts.dto.CustomerDto;
import com.ysf.accounts.entity.Customer;
import com.ysf.accounts.exception.CustomerAlreadyExistsException;
import com.ysf.accounts.exception.NotFoundException;
import com.ysf.accounts.mapper.CustomerMapper;
import com.ysf.accounts.repository.CustomerRepository;
import com.ysf.accounts.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto createNewCustomer(CustomerDto customerDto) {
        Optional<Customer> customerOptional = this.customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (customerOptional.isPresent()) {
            throw new CustomerAlreadyExistsException();
        }

        Customer customer = this.customerMapper.toCustomerEntity(customerDto);
        Customer savedCustomer = this.customerRepository.save(customer);

        return this.customerMapper.toCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto getCustomerByMobileNumber(String mobileNumber) {
        Customer customer = this.fetchCustomerByMobileNumber(mobileNumber);
        return this.customerMapper.toCustomerDto(customer);
    }

    @Override
    public void updateCustomer(String mobileNumber, CustomerDto customerDto) {
        Customer customerToUpdate = this.fetchCustomerByMobileNumber(mobileNumber);
        customerToUpdate = populateCustomerFieldsToBeUpdated(customerToUpdate, customerDto);

        this.customerRepository.save(customerToUpdate);
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        this.customerRepository.deleteById(customerId);
    }

    /**************************************** PRIVATE METHODS ****************************************/

    private Customer fetchCustomerByMobileNumber(String mobileNumber) {
        Optional<Customer> customerOptional = this.customerRepository.findByMobileNumber(mobileNumber);

        return customerOptional.orElseThrow(() -> {
            String errorMsg = "Customer with the given mobile number not found";
            return new NotFoundException(errorMsg);
        });
    }

    private static Customer populateCustomerFieldsToBeUpdated(Customer customerToUpdate, CustomerDto updatedCustomerInfo) {
        if (updatedCustomerInfo == null) {
            return customerToUpdate;
        }

        if (StringUtils.hasText(updatedCustomerInfo.getName())) {
            customerToUpdate.setName(updatedCustomerInfo.getName());
        }
        if (StringUtils.hasText(updatedCustomerInfo.getEmail())) {
            customerToUpdate.setEmail(updatedCustomerInfo.getEmail());
        }
        if (StringUtils.hasText(updatedCustomerInfo.getMobileNumber())) {
            customerToUpdate.setMobileNumber(updatedCustomerInfo.getMobileNumber());
        }

        return customerToUpdate;
    }
}
