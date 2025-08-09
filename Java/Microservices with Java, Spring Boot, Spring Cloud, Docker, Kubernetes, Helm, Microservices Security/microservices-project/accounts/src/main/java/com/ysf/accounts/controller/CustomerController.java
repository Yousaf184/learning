package com.ysf.accounts.controller;

import com.ysf.accounts.dto.CustomerDetailsDto;
import com.ysf.accounts.dto.ResponseDto;
import com.ysf.accounts.service.ICustomerDetailsService;
import com.ysf.accounts.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerDetailsService customerDetailsService;

    @GetMapping("/{mobileNumber}")
    public ResponseEntity<ResponseDto> getCompleteCustomerDetails(
            @PathVariable("mobileNumber") String mobileNumber
    ) throws Exception {
        CustomerDetailsDto customerDetails = this.customerDetailsService.getCompleteCustomerDetails(mobileNumber);
        return ResponseUtils.getSuccessResponse(customerDetails);
    }
}
