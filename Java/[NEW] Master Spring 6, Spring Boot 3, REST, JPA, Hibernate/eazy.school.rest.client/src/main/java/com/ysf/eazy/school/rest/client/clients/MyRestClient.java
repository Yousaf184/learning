package com.ysf.eazy.school.rest.client.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MyRestClient<T> implements IRestClient<T> {

    @Override
    public ResponseEntity<T> getAll(String requestUrl, Class<T> responseClassType) {
        return RestClient.create()
                .get()
                .uri(requestUrl)
                .retrieve()
                .toEntity(responseClassType);
//                .toEntity(new ParameterizedTypeReference<List<T>>() {});
    }
}
