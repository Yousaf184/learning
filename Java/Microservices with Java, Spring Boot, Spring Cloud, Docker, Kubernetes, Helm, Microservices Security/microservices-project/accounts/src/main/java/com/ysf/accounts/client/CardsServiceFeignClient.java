package com.ysf.accounts.client;

import com.ysf.accounts.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Cards", path = "/api/v1/card", fallback = CardFeignClientFallback.class)
public interface CardsServiceFeignClient {

    @GetMapping("/{mobileNumber}")
    ResponseEntity<ResponseDto> getCard(@PathVariable("mobileNumber") String mobileNumber);
}

@Component
class CardFeignClientFallback implements CardsServiceFeignClient {

    @Override
    public ResponseEntity<ResponseDto> getCard(String mobileNumber) {
        return null;
    }
}
