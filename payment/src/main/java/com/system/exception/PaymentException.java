package com.system.exception;

import lombok.Getter;

@Getter
public class PaymentException extends BaseException {

    private final PaymentExceptionType exceptionType;

    public PaymentException( PaymentExceptionType exceptionType ) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
