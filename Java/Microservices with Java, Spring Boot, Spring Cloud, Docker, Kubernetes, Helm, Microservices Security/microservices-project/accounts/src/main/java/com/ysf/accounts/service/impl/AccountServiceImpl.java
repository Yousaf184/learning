package com.ysf.accounts.service.impl;

import com.ysf.accounts.dto.AccountDetailsDto;
import com.ysf.accounts.dto.AccountDto;
import com.ysf.accounts.dto.CustomerDto;
import com.ysf.accounts.entity.Account;
import com.ysf.accounts.exception.BadUserRequestException;
import com.ysf.accounts.exception.NotFoundException;
import com.ysf.accounts.mapper.AccountMapper;
import com.ysf.accounts.repository.AccountRepository;
import com.ysf.accounts.service.IAccountService;
import com.ysf.accounts.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
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

    @Override
    public AccountDetailsDto getAccountDetails(String mobileNumber) {
        CustomerDto customerDto = this.customerService.getCustomerByMobileNumber(mobileNumber);

        Account account = this.fetchAccountByCustomerId(customerDto.getId());
        AccountDto accountDto = this.accountMapper.toAccountDto(account);

        return AccountDetailsDto.builder()
                .customerDetails(customerDto)
                .accountDetails(accountDto)
                .build();
    }

    @Override
    public void updateAccountDetails(String mobileNumber, AccountDetailsDto accountDetails) {
        boolean isCustomerDetailsUpdated = false;
        boolean isAccountDetailsUpdated = false;

        String updatedMobileNumber = null;

        if (accountDetails.getCustomerDetails() != null) {
            CustomerDto updatedCustomerDetails = accountDetails.getCustomerDetails();
            this.customerService.updateCustomer(mobileNumber, updatedCustomerDetails);

            isCustomerDetailsUpdated = true;

            if (updatedCustomerDetails.getMobileNumber() != null) {
                updatedMobileNumber = updatedCustomerDetails.getMobileNumber();
            }
        }

        if (accountDetails.getAccountDetails() != null) {
            if (updatedMobileNumber != null) {
                mobileNumber = updatedMobileNumber;
            }
            CustomerDto customerDto = this.customerService.getCustomerByMobileNumber(mobileNumber);
            this.updateAccount(customerDto.getId(), accountDetails.getAccountDetails());
            isAccountDetailsUpdated = true;
        }

        if (!isCustomerDetailsUpdated && !isAccountDetailsUpdated) {
            String errorMsg = "Update Failed. Nothing to update in the request";
            throw new BadUserRequestException(errorMsg);
        }
    }

    @Override
    public void deleteAccountDetails(String mobileNumber) {
        CustomerDto customerDto = this.customerService.getCustomerByMobileNumber(mobileNumber);
        this.accountRepository.deleteByCustomerId(customerDto.getId());
        this.customerService.deleteCustomerById(customerDto.getId());
    }

    /**************************************** PRIVATE METHODS ****************************************/

    private Account fetchAccountByCustomerId(Long customerId) {
        Optional<Account> accountOptional = this.accountRepository.findByCustomerId(customerId);

        return accountOptional.orElseThrow(() -> {
            String errorMsg = "Account with the given mobile number not found";
            return new NotFoundException(errorMsg);
        });
    }

    private void updateAccount(Long customerId, AccountDto updatedAccountInfo) {
        Account accountToBeUpdated = this.fetchAccountByCustomerId(customerId);
        accountToBeUpdated = populateAccountFieldsToBeUpdated(accountToBeUpdated, updatedAccountInfo);

        this.accountRepository.save(accountToBeUpdated);
    }

    private static Account populateAccountFieldsToBeUpdated(Account accountToBeUpdated, AccountDto updatedAccountInfo) {
        if (updatedAccountInfo == null) {
            return accountToBeUpdated;
        }

        if (StringUtils.hasText(updatedAccountInfo.getAccountType())) {
            accountToBeUpdated.setAccountType(updatedAccountInfo.getAccountType());
        }
        if (StringUtils.hasText(updatedAccountInfo.getBranchAddress())) {
            accountToBeUpdated.setBranchAddress(updatedAccountInfo.getBranchAddress());
        }

        return accountToBeUpdated;
    }
}
