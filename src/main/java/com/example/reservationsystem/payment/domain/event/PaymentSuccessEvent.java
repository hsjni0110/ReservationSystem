package com.example.reservationsystem.payment.domain.event;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;

import java.time.LocalDateTime;

public record PaymentSuccessEvent(
        Long paymentId,
        EventType eventType,
        EventStatus eventStatus,
        LocalDateTime eventDate,
        // payload
        Long userId,
        Long reservationId,
        Long paymentAmount
) implements AggregateEvent {

    public PaymentSuccessEvent( Long paymentId, Long userId, Long reservationId, Long paymentAmount ) {
        this( paymentId, EventType.PAYMENT_SUCCESS, EventStatus.INIT, LocalDateTime.now(), userId, reservationId, paymentAmount );
    }

    @Override
    public Long getAggregateId() {
        return paymentId;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    @Override
    public LocalDateTime getEventDate() {
        return eventDate;
    }

}
