package com.example.reservationsystem.payment.event;

import com.example.reservationsystem.common.domain.AggregateEvent;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentAttemptEvent(
        EventType eventType,
        EventStatus eventStatus,
        Long paymentId,
        Long userId,
        BigDecimal totalPrice,
        LocalDateTime createdAt
) implements AggregateEvent {

    public PaymentAttemptEvent( Long paymentId, Long userId, BigDecimal totalPrice ) {
        this( EventType.PAYMENT_ATTEMPT, EventStatus.INIT, paymentId, userId, totalPrice, LocalDateTime.now() );
    }

    @Override
    public Long getAggregateId() {
        return this.paymentId;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public EventStatus getEventStatus() {
        return this.eventStatus;
    }

    @Override
    public LocalDateTime getEventDate() {
        return createdAt;
    }

}