package com.example.reservationsystem.account.domain.event;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;

import java.time.LocalDateTime;

public record InsufficientAmountEvent(
        Long accountId,
        EventType eventType,
        EventStatus eventStatus,
        LocalDateTime eventDate,
        // payload
        Long userId,
        Long reservationId
) implements AggregateEvent {

    public InsufficientAmountEvent( Long accountId, Long userId, Long reservationId ) {
        this( accountId, EventType.INSUFFICIENT_BALANCE, EventStatus.INIT, LocalDateTime.now(), userId, reservationId );
    }

    @Override
    public Long getAggregateId() {
        return accountId;
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
