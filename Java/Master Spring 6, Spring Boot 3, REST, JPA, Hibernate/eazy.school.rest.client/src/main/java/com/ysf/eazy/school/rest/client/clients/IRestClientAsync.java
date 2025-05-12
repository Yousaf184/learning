package com.ysf.eazy.school.rest.client.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IRestClientAsync {

    <T> Mono<ResponseEntity<T>> getAllAsync(String requestUrl, Class<T> responseClassType);

    <B, S, E> Mono<ResponseEntity<?>> postAsync(
        String requestUrl,
        B requestBody,
        HttpHeaders customRequestHeaders,
        Class<S> successResponseClassType,
        Class<E> errorResponseClassType
    );
}
