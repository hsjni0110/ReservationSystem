package com.system.domain.event;

import com.system.domain.AggregateEvent;
import com.system.type.EventStatus;
import com.system.type.EventType;

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
