package com.ysf.spring6.rest.mvc.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBeanValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMsg = fieldError.getDefaultMessage();

                    if (errorsMap.containsKey(fieldName)) {
                        errorsMap.get(fieldName).add(errorMsg);
                    } else if (errorMsg != null) {
                        List<String> errorList = new ArrayList<>();
                        errorList.add(errorMsg);
                        errorsMap.put(fieldName, errorList);
                    }
                });

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "error");
        responseMap.put("errors", errorsMap);

        return ResponseEntity.badRequest().body(responseMap);
    }
}
