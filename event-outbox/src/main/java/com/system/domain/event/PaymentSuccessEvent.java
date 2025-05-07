package com.system.domain.event;

import com.system.domain.AggregateEvent;
import com.system.type.EventStatus;
import com.system.type.EventType;

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
