package com.example.reservationsystem.payment.exception;

import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.common.exception.BaseException;
import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
