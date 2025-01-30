package com.ysf.eazy.school.rest.client.clients;

import org.springframework.http.ResponseEntity;

public interface IRestClient<T> {
    ResponseEntity<T> getAll(String requestUrl, Class<T> responseClassType);
}
