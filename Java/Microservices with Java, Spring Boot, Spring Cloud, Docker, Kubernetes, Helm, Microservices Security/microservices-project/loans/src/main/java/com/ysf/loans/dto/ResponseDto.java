package com.ysf.loans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ysf.loans.utils.ResponseUtils;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private ResponseUtils.ResponseStatus status;
    private String message;
    private Object data;
    private Object errors;
}
