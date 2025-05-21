package com.ysf.accounts.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    private enum ResponseStatus { SUCCESS, ERROR }

    public static ResponseEntity<Map<String, Object>> getSuccessResponse(Object responseBody, String responseMsg) {
        return ResponseUtils.createGeneralSuccessResponse(responseBody, responseMsg);
    }

    public static ResponseEntity<Map<String, Object>> getSuccessResponse(String responseMsg) {
        return ResponseUtils.createGeneralSuccessResponse(null, responseMsg);
    }

    public static ResponseEntity<Map<String, Object>> getSuccessResponse(Object responseBody) {
        return ResponseUtils.createGeneralSuccessResponse(responseBody, null);
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
        Map<String, Object> responseMap = new HashMap<>();

        ResponseStatus status = errorResponseMsg != null
                ? ResponseStatus.ERROR
                : ResponseStatus.SUCCESS;

        String message = null;
        if (errorResponseMsg != null) {
            message = errorResponseMsg;
        } else if (successResponseMsg != null) {
            message = successResponseMsg;
        }

        responseMap.put("status", status);
        if (message != null) {
            responseMap.put("message", message);
        }
        if (responseBody != null) {
            responseMap.put("data", responseBody);
        }

        return ResponseEntity.status(statusCode).body(responseMap);
    }
}
