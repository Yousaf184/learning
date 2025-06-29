package com.ysf.accounts.utils;

import com.ysf.accounts.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public enum ResponseStatus { SUCCESS, ERROR }

    public static ResponseEntity<ResponseDto> getSuccessResponse(Object responseBody, String responseMsg) {
        return ResponseUtils.createGeneralSuccessResponse(responseBody, responseMsg);
    }

    public static ResponseEntity<ResponseDto> getSuccessResponse(String responseMsg) {
        return ResponseUtils.createGeneralSuccessResponse(null, responseMsg);
    }

    public static ResponseEntity<ResponseDto> getSuccessResponse(Object responseBody) {
        return ResponseUtils.createGeneralSuccessResponse(responseBody, null);
    }

    public static ResponseEntity<ResponseDto> getCreatedSuccessResponse(Object responseBody, String responseMsg) {
        return ResponseUtils.createResponse(HttpStatus.CREATED, responseBody, responseMsg, null);
    }

    public static ResponseEntity<ResponseDto> getErrorResponse(HttpStatus errorStatusCode, Object responseBody) {
        return ResponseUtils.createGeneralErrorResponse(errorStatusCode, responseBody, null);
    }

    public static ResponseEntity<ResponseDto> getErrorResponse(HttpStatus errorStatusCode, String errorMsg) {
        return ResponseUtils.createGeneralErrorResponse(errorStatusCode, null, errorMsg);
    }

    /**************************************** PRIVATE METHODS ****************************************/

    private static ResponseEntity<ResponseDto> createGeneralSuccessResponse(Object responseBody, String successMsg) {
        return ResponseUtils.createResponse(HttpStatus.OK, responseBody, successMsg, null);
    }

    private static ResponseEntity<ResponseDto> createGeneralErrorResponse(HttpStatus errorStatusCode, Object responseBody, String errorMsg) {
        return ResponseUtils.createResponse(errorStatusCode, responseBody, null, errorMsg);
    }

    private static ResponseEntity<ResponseDto> createResponse(
        HttpStatus statusCode,
        Object responseBody,
        String successResponseMsg,
        String errorResponseMsg
    ) {
        ResponseStatus status = statusCode.isError()
                ? ResponseStatus.ERROR
                : ResponseStatus.SUCCESS;

        String message = null;
        if (errorResponseMsg != null) {
            message = errorResponseMsg;
        } else if (successResponseMsg != null) {
            message = successResponseMsg;
        }

        ResponseDto responseDto = ResponseDto.builder()
                .status(status)
                .message(message)
                .data(statusCode.isError() ? null : responseBody)
                .errors(statusCode.isError() ? responseBody : null)
                .build();

        return ResponseEntity.status(statusCode).body(responseDto);
    }
}
