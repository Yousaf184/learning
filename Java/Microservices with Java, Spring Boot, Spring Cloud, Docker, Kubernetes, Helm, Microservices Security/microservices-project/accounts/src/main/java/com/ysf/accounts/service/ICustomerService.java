package com.ysf.accounts.service;

import com.ysf.accounts.dto.CustomerDto;

public interface ICustomerService {

    CustomerDto createNewCustomer(CustomerDto customerDto);
}
