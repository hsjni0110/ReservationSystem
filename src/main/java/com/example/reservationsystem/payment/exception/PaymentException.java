package com.example.reservationsystem.payment.exception;

import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.common.exception.BaseException;
import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PaymentException extends BaseException {

    private final PaymentExceptionType exceptionType;
    private Long accountId;
    private Money currentAmount;
    private Money requestedAmount;

    public PaymentException(PaymentExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public PaymentException(PaymentExceptionType exceptionType, Long accountId, Money currentAmount, Money requestedAmount) {
        this.exceptionType = exceptionType;
        this.accountId = accountId;
        this.currentAmount = currentAmount;
        this.requestedAmount = requestedAmount;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
