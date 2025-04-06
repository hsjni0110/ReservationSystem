package com.example.reservationsystem.common.domain;

import com.example.reservationsystem.common.domain.repository.EventOutboxRepository;
import com.example.reservationsystem.common.exception.EventException;
import com.example.reservationsystem.common.exception.EventExceptionType;
import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;
import com.example.reservationsystem.infrastructure.publisher.EventPublisher;
import com.example.reservationsystem.payment.event.PaymentAttemptEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.reservationsystem.common.exception.EventExceptionType.EVENT_NOT_FOUND;
import static com.example.reservationsystem.common.utils.JsonUtils.toJson;

@Service
public class EventOutboxService {

    private final EventPublisher eventPublisher;
    private final EventOutboxRepository eventOutboxRepository;

    public EventOutboxService(@Qualifier("kafka") EventPublisher eventPublisher, EventOutboxRepository eventOutboxRepository) {
        this.eventPublisher = eventPublisher;
        this.eventOutboxRepository = eventOutboxRepository;
    }

    public void save(AggregateType aggregateType, AggregateEvent aggregateEvent, Long aggregateId, EventType eventType, LocalDateTime createdAt, EventStatus eventStatus ) {
        OutboxMessage message = OutboxMessage.builder()
                .aggregateType( aggregateType )
                .aggregateId( aggregateId )
                .payload( toJson(aggregateEvent) )
                .eventType( eventType )
                .eventStatus( eventStatus )
                .retryCount( 0 )
                .eventDate( createdAt )
                .build();

        eventOutboxRepository.save( message );
    }

    public void publishEvent( AggregateEvent event ) {
        eventPublisher.publishEvent( event );
    }

    public void recordEvent( PaymentAttemptEvent event ) {
        OutboxMessage outboxMessage = findByEvent(event);
        outboxMessage.recordSuccess();
    }

    public OutboxMessage findByEvent( AggregateEvent event) {
        return eventOutboxRepository.findByEvent(
                event.getEventType(),
                event.getEventStatus(),
                event.getEventDate(),
                event.getAggregateId()
        ).orElseThrow( () -> new EventException( EVENT_NOT_FOUND ) );
    }

}
