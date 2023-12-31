package com.erp.webtoon.exception.EntityNotFound;

import com.erp.webtoon.global.exception.BaseException;
import com.erp.webtoon.global.exception.BaseExceptionType;

public class UserException extends BaseException {

    private BaseExceptionType exceptionType;

    public UserException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.exceptionType;
    }
}
