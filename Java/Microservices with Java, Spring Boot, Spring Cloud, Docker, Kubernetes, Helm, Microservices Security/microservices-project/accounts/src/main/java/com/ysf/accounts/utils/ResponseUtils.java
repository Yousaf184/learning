package com.ysf.accounts.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    public static ResponseEntity<Map<String, Object>> getSuccessResponse(Object responseBody, String responseMsg) {
        return ResponseUtils.createGeneralSuccessResponse(responseBody, responseMsg);
    }

    public static ResponseEntity<Map<String, Object>> getSuccessResponse(String responseMsg) {
        return ResponseUtils.createGeneralSuccessResponse(null, responseMsg);
    }

    public static ResponseEntity<Map<String, Object>> getCreatedSuccessResponse(Object responseBody, String responseMsg) {
        return ResponseUtils.createResponse(HttpStatus.CREATED, responseBody, responseMsg, null);
    }

    public static ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus errorStatusCode, Object responseBody, String errorMsg) {
        return ResponseUtils.createGeneralErrorResponse(errorStatusCode, responseBody, errorMsg);
    }

    public static ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus errorStatusCode, String errorMsg) {
        return ResponseUtils.createGeneralErrorResponse(errorStatusCode, null, errorMsg);
    }

    /**************************************** PRIVATE METHODS ****************************************/

    private static ResponseEntity<Map<String, Object>> createGeneralSuccessResponse(Object responseBody, String successMsg) {
        return ResponseUtils.createResponse(HttpStatus.OK, responseBody, successMsg, null);
    }

    private static ResponseEntity<Map<String, Object>> createGeneralErrorResponse(HttpStatus errorStatusCode, Object responseBody, String errorMsg) {
        return ResponseUtils.createResponse(errorStatusCode, responseBody, null, errorMsg);
    }

    private static ResponseEntity<Map<String, Object>> createResponse(
        HttpStatus statusCode,
        Object responseBody,
        String successResponseMsg,
        String errorResponseMsg
    ) {
        if (successResponseMsg == null && errorResponseMsg == null) {
            String errorMsg = "Both success and error response message cannot be null at the same time";
            throw new IllegalArgumentException(errorMsg);
        }

        if (successResponseMsg != null && errorResponseMsg != null) {
            String errorMsg = "Both success and error response message cannot be non-null at the same time";
            throw new IllegalArgumentException(errorMsg);
        }

        Map<String, Object> responseMap = new HashMap<>();

        String status = successResponseMsg != null ? "Success" : "Error";
        String message = successResponseMsg != null ? successResponseMsg : errorResponseMsg;

        responseMap.put("status", status);
        responseMap.put("message", message);
        if (responseBody != null) {
            responseMap.put("data", responseBody);
        }

        return ResponseEntity.status(statusCode).body(responseMap);
    }
}
