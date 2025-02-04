package com.ysf.eazy.school.rest.client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ErrorResponse {

    private String status;
    private List<Error> errors;

    @Getter
    @Setter
    @ToString
    public static class Error {
        private String field;
        private String message;
    }
}

