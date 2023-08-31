package com.erp.webtoon.global.exception;

import com.erp.webtoon.dto.excetion.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {

    /**
     * BaseException 예외 처리
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleException(BaseException exception) {
        BaseExceptionType exType = exception.getExceptionType();
        ErrorResponse body = ErrorResponse.builder()
                .code(exType.getErrorCode())
                .httpStatus(exType.getHttpStatus())
                .errorMessage(exType.getErrorMessage())
                .build();

        return ResponseEntity.status(exType.getHttpStatus())
                .body(body);
    }

    /**
     * EntityNotFound 예외 처리
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(EntityNotFoundException exception) {

        ErrorResponse body = ErrorResponse.builder()
                .code(404)
                .httpStatus(HttpStatus.NOT_FOUND)
                .errorMessage(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    /**
     * NoSuchElement 예외 처리
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException exception) {

        ErrorResponse body = ErrorResponse.builder()
                .code(404)
                .httpStatus(HttpStatus.NOT_FOUND)
                .errorMessage(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    /**
     * IllegalState 예외 처리
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException exception) {

        ErrorResponse body = ErrorResponse.builder()
                .code(400)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errorMessage(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    /**
     * IllegalArgument 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {

        ErrorResponse body = ErrorResponse.builder()
                .code(400)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errorMessage(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    /**
     * @Valid 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgument(MethodArgumentNotValidException exception) {
        FieldError error = exception.getFieldErrors().get(0);

        ErrorResponse body = ErrorResponse.builder()
                .code(401)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .field(error.getField())
                .errorMessage(error.getDefaultMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

}
