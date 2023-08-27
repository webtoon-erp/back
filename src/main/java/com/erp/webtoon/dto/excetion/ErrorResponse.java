package com.erp.webtoon.dto.excetion;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    /**
     * {
     *     code :
     *     httpStatus :
     *     field :
     *     errorMessage :
     * }
     */

    private int code;

    private HttpStatus httpStatus;

    private String field;

    private String errorMessage;

    @Builder
    public ErrorResponse(int code, HttpStatus httpStatus, String field, String errorMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.field = field;
        this.errorMessage = errorMessage;
    }

    @Builder
    public ErrorResponse(int code, HttpStatus httpStatus, String errorMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
