package com.example.reservationsystem.account.domain.event;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;

import java.time.LocalDateTime;

public record AccountDebitFailedEvent(
        Long userId,
        EventType eventType,
        EventStatus eventStatus,
        LocalDateTime eventDate,
        // payload
        Long reservationId
) implements AggregateEvent {

    public AccountDebitFailedEvent( Long userId, Long reservationId ) {
        this( userId, EventType.ACCOUNT_DEBITED_FAILURE, EventStatus.INIT, LocalDateTime.now(), reservationId );
    }

    @Override
    public Long getAggregateId() {
        return userId;
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
