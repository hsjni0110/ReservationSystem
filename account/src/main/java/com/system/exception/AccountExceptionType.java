package com.system.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum AccountExceptionType implements BaseExceptionType {

    ACCOUNT_NOT_FOUND(NOT_FOUND, "Account not found"),
    INVALID_RECHARGED_PRICE(BAD_REQUEST, "Invalid recharged price"),
    AMOUNT_IS_NOT_SUFFICIENT(UNAUTHORIZED, "amount is not sufficient"),
    CONCURRENCY_MODIFICATION(BAD_REQUEST, "동시성 오류 발생"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return message;
    }

}
