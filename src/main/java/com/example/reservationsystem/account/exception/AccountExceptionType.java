package com.example.reservationsystem.account.exception;

import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
public enum AccountExceptionType implements BaseExceptionType {

    ACCOUNT_NOT_FOUND(NOT_FOUND, "Account not found"),
    INVALID_RECHARGED_PRICE(BAD_REQUEST, "Invalid recharged price"),
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
