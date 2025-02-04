package com.ysf.eazy.school.rest.client.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface IRestClient {
    <T> ResponseEntity<T> getAll(String requestUrl, Class<T> responseClassType);

    <B, S, E> ResponseEntity<?> post(
        String requestUrl,
        B requestBody,
        HttpHeaders customRequestHeaders,
        Class<S> successResponseClassType,
        Class<E> errorResponseClassType
    );
}
