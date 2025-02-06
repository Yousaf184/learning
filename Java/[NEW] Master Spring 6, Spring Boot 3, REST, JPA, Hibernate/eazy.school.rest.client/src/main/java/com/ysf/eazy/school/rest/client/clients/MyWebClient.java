package com.ysf.eazy.school.rest.client.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MyWebClient implements IRestClientAsync {

    @Override
    public <T> Mono<ResponseEntity<T>> getAllAsync(String requestUrl, Class<T> responseClassType) {
        log.info("ASYNC GET request using WebClient");

        WebClient webClient = WebClient.create();

        return webClient.get()
                .uri(requestUrl)
                .retrieve()
                .toEntity(responseClassType);
    }

    @Override
    public <B, S, E> Mono<ResponseEntity<?>> postAsync(
        String requestUrl,
        B requestBody,
        HttpHeaders customRequestHeaders,
        Class<S> successResponseClassType,
        Class<E> errorResponseClassType
    ) {
        log.info("ASYNC POST request using WebClient");

        WebClient webClient = WebClient.create();

        return webClient.post()
                .uri(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .headers(requestHeaders -> requestHeaders.addAll(customRequestHeaders))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        Mono<S> successResponseBody = response.bodyToMono(successResponseClassType);
                        return successResponseBody.map(responseBody -> ResponseEntity
                                .status(response.statusCode().value())
                                .body(responseBody));
                    } else {
                        Mono<E> errorResponseBody = response.bodyToMono(errorResponseClassType);
                        return errorResponseBody.map(responseBody -> ResponseEntity
                                .status(response.statusCode().value())
                                .body(responseBody));
                    }
                });
    }
}
