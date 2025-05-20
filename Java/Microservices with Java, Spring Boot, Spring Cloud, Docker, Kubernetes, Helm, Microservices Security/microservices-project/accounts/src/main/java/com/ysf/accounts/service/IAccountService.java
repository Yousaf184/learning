package com.ysf.accounts.service;

import com.ysf.accounts.dto.AccountDto;
import com.ysf.accounts.dto.CustomerDto;

public interface IAccountService {

    AccountDto createNewAccount(CustomerDto customerDto);
}
