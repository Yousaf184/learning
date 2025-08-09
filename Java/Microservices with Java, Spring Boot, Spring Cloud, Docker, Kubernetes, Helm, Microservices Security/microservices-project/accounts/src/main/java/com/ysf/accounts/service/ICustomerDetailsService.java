package com.ysf.accounts.service;

import com.ysf.accounts.dto.CustomerDetailsDto;

public interface ICustomerDetailsService {

    CustomerDetailsDto getCompleteCustomerDetails(String mobileNumber) throws Exception;
}
