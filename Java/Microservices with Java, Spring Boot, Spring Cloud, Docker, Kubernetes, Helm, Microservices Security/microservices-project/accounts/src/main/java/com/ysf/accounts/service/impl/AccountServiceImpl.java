package com.ysf.accounts.service.impl;

import com.ysf.accounts.dto.AccountDto;
import com.ysf.accounts.dto.CustomerDto;
import com.ysf.accounts.entity.Account;
import com.ysf.accounts.mapper.AccountMapper;
import com.ysf.accounts.repository.AccountRepository;
import com.ysf.accounts.service.IAccountService;
import com.ysf.accounts.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ICustomerService customerService;

    @Override
    public AccountDto createNewAccount(CustomerDto customerDto) {
        CustomerDto savedCustomerDto = this.customerService.createNewCustomer(customerDto);

        // 10-digit number
        long generatedAccountNumber = 1_000_000_000L + (long)(new Random().nextDouble() * 9_000_000_000L);
        Account newAccount = Account.builder()
                .accountNumber(generatedAccountNumber)
                .customerId(savedCustomerDto.getId())
                .accountType("CURRENT")
                .branchAddress("123 Test Street, NY, USA")
                .build();

        Account savedAccount = this.accountRepository.save(newAccount);

        return this.accountMapper.toAccountDto(savedAccount);
    }
}
