package com.ysf.accounts.service;

import com.ysf.accounts.dto.AccountDetailsDto;
import com.ysf.accounts.dto.AccountDto;
import com.ysf.accounts.dto.CustomerDto;

public interface IAccountService {

    AccountDto createNewAccount(CustomerDto customerDto);

    AccountDetailsDto getAccountDetails(String mobileNumber);

    void updateAccountDetails(String mobileNumber, AccountDetailsDto accountDetails);

    void deleteAccountDetails(String mobileNumber);
}
