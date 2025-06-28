package com.ysf.accounts.controller;

import com.ysf.accounts.dto.AccountDetailsDto;
import com.ysf.accounts.dto.AccountDto;
import com.ysf.accounts.dto.CustomerDto;
import com.ysf.accounts.service.IAccountService;
import com.ysf.accounts.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @PostMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> createNewAccount(@Valid @RequestBody CustomerDto customerDetails) {
        AccountDto createdAccount = this.accountService.createNewAccount(customerDetails);

        String successResponseMsg = "Account created successfully";
        return ResponseUtils.getCreatedSuccessResponse(createdAccount, successResponseMsg);
    }

    @GetMapping("/{mobileNumber}")
    public ResponseEntity<Map<String, Object>> getAccountDetails(@PathVariable("mobileNumber") String mobileNumber) {
        AccountDetailsDto accountDetails = this.accountService.getAccountDetails(mobileNumber);
        return ResponseUtils.getSuccessResponse(accountDetails);
    }

    @PutMapping("/{mobileNumber}")
    public ResponseEntity<Map<String, Object>> updateAccountDetails(
        @PathVariable("mobileNumber") String mobileNumber,
        @RequestBody AccountDetailsDto accountDetailsDto
    ) {
        this.accountService.updateAccountDetails(mobileNumber, accountDetailsDto);

        String responseMsg = "Details updated successfully";
        return ResponseUtils.getSuccessResponse(responseMsg);
    }

    @DeleteMapping("/{mobileNumber}")
    public ResponseEntity<Map<String, Object>> deleteAccountDetails(@PathVariable("mobileNumber") String mobileNumber) {
        this.accountService.deleteAccountDetails(mobileNumber);

        String responseMsg = "Account deleted successfully";
        return ResponseUtils.getSuccessResponse(responseMsg);
    }
}

