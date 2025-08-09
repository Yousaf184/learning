package com.ysf.accounts.client;

import com.ysf.accounts.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Cards", path = "/api/v1/card")
public interface CardsServiceFeignClient {

    @GetMapping("/{mobileNumber}")
    ResponseEntity<ResponseDto> getCard(@PathVariable("mobileNumber") String mobileNumber);
}
