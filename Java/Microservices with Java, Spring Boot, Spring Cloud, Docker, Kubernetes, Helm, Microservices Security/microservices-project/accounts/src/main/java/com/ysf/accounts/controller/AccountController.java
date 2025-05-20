package com.ysf.accounts.controller;

import com.ysf.accounts.dto.AccountDto;
import com.ysf.accounts.dto.CustomerDto;
import com.ysf.accounts.service.IAccountService;
import com.ysf.accounts.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @PostMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> createNewAccount(@RequestBody CustomerDto customerDetails) {
        AccountDto createdAccount = this.accountService.createNewAccount(customerDetails);

        String successResponseMsg = "Account created successfully";
        return ResponseUtils.getCreatedSuccessResponse(createdAccount, successResponseMsg);
    }
}
