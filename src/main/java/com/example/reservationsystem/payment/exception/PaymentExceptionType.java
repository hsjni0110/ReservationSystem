package com.example.reservationsystem.payment.exception;

import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
public enum PaymentExceptionType implements BaseExceptionType {

    USER_NOT_MATCHED(UNAUTHORIZED, "user not matched"),
    NOT_PAYABLE(UNAUTHORIZED, "not payable status"),
    AMOUNT_IS_NOT_SUFFICIENT(UNAUTHORIZED, "amount is not sufficient"),
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
