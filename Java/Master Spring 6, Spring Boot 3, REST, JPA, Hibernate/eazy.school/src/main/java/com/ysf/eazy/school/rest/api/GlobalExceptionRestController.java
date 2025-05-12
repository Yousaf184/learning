package com.ysf.eazy.school.rest.api;


import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
@Order(1)
public class GlobalExceptionRestController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode statusCode,
        WebRequest request
    ) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "error");

        List<Map<String, String>> errorsList = new ArrayList<>();
        ex.getAllErrors().forEach(error -> {
            Map<String, String> errorMap = new HashMap<>();
            if (error instanceof FieldError) {
                errorMap.put("field", ((FieldError) error).getField());
            }
            errorMap.put("message", error.getDefaultMessage());
            errorsList.add(errorMap);
        });
        responseMap.put("errors", errorsList);

        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Map<String, Object>> exceptionHandler(Exception exception) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "error");
        responseMap.put("message", exception.getMessage());

        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
