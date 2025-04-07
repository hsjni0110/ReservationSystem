package com.example.reservationsystem.payment.domain.event;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentAttemptEvent(
        EventType eventType,
        EventStatus eventStatus,
        Long paymentId,
        LocalDateTime createdAt,
        // payload
        Long userId,
        Long reservationId,
        BigDecimal totalPrice
) implements AggregateEvent {

    public PaymentAttemptEvent(Long paymentId, Long userId, BigDecimal totalPrice, Long reservationId) {
        this( EventType.PAYMENT_ATTEMPT, EventStatus.INIT, paymentId, LocalDateTime.now(), userId, reservationId, totalPrice );
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