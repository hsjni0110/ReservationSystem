package com.example.reservationsystem.payment.event;

import com.example.reservationsystem.common.domain.AggregateEvent;

import java.math.BigDecimal;

public record PaymentAttemptEvent(
        Long paymentId,
        Long userId,
        BigDecimal totalPrice
) implements AggregateEvent {

    @Override
    public Long getAggregateId() {
        return paymentId;
    }

}