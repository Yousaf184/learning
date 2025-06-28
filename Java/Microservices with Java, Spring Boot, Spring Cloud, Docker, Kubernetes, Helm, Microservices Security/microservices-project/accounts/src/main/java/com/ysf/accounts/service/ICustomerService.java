package com.ysf.accounts.service;

import com.ysf.accounts.dto.CustomerDto;

public interface ICustomerService {

    CustomerDto createNewCustomer(CustomerDto customerDto);

    CustomerDto getCustomerByMobileNumber(String mobileNumber);

    void updateCustomer(String mobileNumber, CustomerDto customerDto);

    void deleteCustomerById(Long customerId);
}
