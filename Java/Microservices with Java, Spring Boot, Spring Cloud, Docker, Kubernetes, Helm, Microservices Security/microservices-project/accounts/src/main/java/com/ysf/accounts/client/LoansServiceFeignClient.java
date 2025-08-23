package com.ysf.accounts.client;

import com.ysf.accounts.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Loans", path = "/api/v1/loan", fallback = LoansFeignClientFallback.class)
public interface LoansServiceFeignClient {

    @GetMapping("/{mobileNumber}")
    ResponseEntity<ResponseDto> getLoan(@PathVariable("mobileNumber") String mobileNumber);
}

@Component
class LoansFeignClientFallback implements LoansServiceFeignClient {

    @Override
    public ResponseEntity<ResponseDto> getLoan(String mobileNumber) {
        return null;
    }
}
