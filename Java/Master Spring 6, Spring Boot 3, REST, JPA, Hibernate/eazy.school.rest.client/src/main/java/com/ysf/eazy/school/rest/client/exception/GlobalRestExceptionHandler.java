package com.ysf.eazy.school.rest.client.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(RestTemplateErrorHandler.CustomRestTemplateException.class)
    public ResponseEntity<String> handleErrorResponseFromRestTemplate(RestTemplateErrorHandler.CustomRestTemplateException ex) {
        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ex.getErrorJson());
    }
}
