package com.ysf.api.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping
    public Mono<ResponseEntity<?>> fallbackResponse() {
        Map<String, String> responseMap = Map.of("message", "Service is down, please try after some time");
        ResponseEntity<Map<String, String>> response = ResponseEntity.internalServerError().body(responseMap);
        return Mono.just(response);
    }
}
