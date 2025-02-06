package com.ysf.eazy.school.rest.client.clients;

import com.ysf.eazy.school.rest.client.exception.RestTemplateErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class MyRestTemplate implements IRestClient {

    private final RestTemplate restTemplate;

    @Autowired
    public MyRestTemplate(RestTemplateErrorHandler restTemplateErrorHandler) {
        this.restTemplate = new RestTemplateBuilder()
                .errorHandler(restTemplateErrorHandler)
                .build();
    }

    @Override
    public <T> ResponseEntity<T> getAll(String requestUrl, Class<T> responseClassType) {
        log.info("GET request using RestTemplate");

        return this.restTemplate.getForEntity(requestUrl, responseClassType);
    }

    @Override
    public <B, S, E> ResponseEntity<?> post(
        String requestUrl,
        B requestBody,
        HttpHeaders customRequestHeaders,
        Class<S> successResponseClassType,
        Class<E> errorResponseClassType
    ) {
        log.info("POST request using RestTemplate");

        HttpEntity<B> httpEntity = new HttpEntity<>(requestBody, customRequestHeaders);
        return this.restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, successResponseClassType);
    }
}
