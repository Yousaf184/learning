package com.ysf.accounts.service.impl;

import com.ysf.accounts.dto.CustomerDto;
import com.ysf.accounts.entity.Customer;
import com.ysf.accounts.exception.CustomerAlreadyExistsException;
import com.ysf.accounts.mapper.CustomerMapper;
import com.ysf.accounts.repository.CustomerRepository;
import com.ysf.accounts.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Optional<CustomerDto> getCustomerByMobileNumber(String mobileNumber) {
        Optional<Customer> customerOptional = this.customerRepository.findByMobileNumber(mobileNumber);
        return customerOptional.map(this.customerMapper::toCustomerDto);
    }
}
