package com.erp.webtoon.exception.EntityNotFound;

import com.erp.webtoon.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {

    NOT_FOUND_EMPLOYEE(404, HttpStatus.NOT_FOUND, "존재하지 않는 사번입니다."),
    NOT_FOUND_NOTICE(404, HttpStatus.NOT_FOUND, "존재하지 않는 공지사항입니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    UserExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
