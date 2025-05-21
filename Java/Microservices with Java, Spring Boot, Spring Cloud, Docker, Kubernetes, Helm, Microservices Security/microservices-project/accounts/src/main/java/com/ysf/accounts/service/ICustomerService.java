package com.ysf.accounts.service;

import com.ysf.accounts.dto.CustomerDto;

import java.util.Optional;

public interface ICustomerService {

    CustomerDto createNewCustomer(CustomerDto customerDto);

    Optional<CustomerDto> getCustomerByMobileNumber(String mobileNumber);
}
