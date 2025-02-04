package com.ysf.eazy.school.rest.client.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class MyRestClient implements IRestClient {

    @Override
    public <T> ResponseEntity<T> getAll(String requestUrl, Class<T> responseClassType) {
        return RestClient.create()
                .get()
                .uri(requestUrl)
                .retrieve()
                .toEntity(responseClassType);
//                .toEntity(new ParameterizedTypeReference<List<T>>() {});
    }

    @Override
    public <B, S, E> ResponseEntity<?> post(
        String requestUrl,
        B requestBody,
        HttpHeaders customRequestHeaders,
        Class<S> successResponseClassType,
        Class<E> errorResponseClassType
    ) {
        return RestClient.create()
                .post()
                .uri(requestUrl)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(requestHeaders -> requestHeaders.addAll(customRequestHeaders))
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        S successResponseBody = new ObjectMapper().readValue(response.getBody(), successResponseClassType);
                        System.out.println(successResponseBody);
                        return ResponseEntity
                                .status(response.getStatusCode().value())
                                .body(successResponseBody);
                    } else {
                        E errorResponseBody = new ObjectMapper().readValue(response.getBody(), errorResponseClassType);
                        return ResponseEntity
                                .status(response.getStatusCode().value())
                                .body(errorResponseBody);
                    }
                });
    }
}
