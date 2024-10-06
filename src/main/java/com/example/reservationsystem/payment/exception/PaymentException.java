package com.example.reservationsystem.payment.exception;

import com.example.reservationsystem.common.exception.BaseException;
import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentException extends BaseException {

    private final PaymentExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
