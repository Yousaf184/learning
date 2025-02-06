package com.ysf.eazy.school.rest.client.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysf.eazy.school.rest.client.model.CustomErrorResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode responseStatusCode = response.getStatusCode();
        return responseStatusCode.is4xxClientError() || responseStatusCode.is5xxServerError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomErrorResponse errorResponse = objectMapper.readValue(response.getBody(), CustomErrorResponse.class);

        String errorStr = objectMapper.writeValueAsString(errorResponse);
        HttpStatusCode responseStatusCode = response.getStatusCode();

        throw new CustomRestTemplateException(errorStr, responseStatusCode);
    }

    @Getter
    @Setter
    public static class CustomRestTemplateException extends IOException {
        private final String errorJson;
        private HttpStatusCode httpStatusCode;

        public CustomRestTemplateException(String errorJson, HttpStatusCode httpStatusCode) {
            super(errorJson);
            this.errorJson = errorJson;
            this.httpStatusCode = httpStatusCode;
        }
    }

}
