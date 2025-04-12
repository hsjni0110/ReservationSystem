package com.example.reservationsystem.common.domain.model;

import com.example.reservationsystem.common.exception.EventException;
import com.example.reservationsystem.common.infra.repository.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.reservationsystem.common.exception.EventExceptionType.EVENT_NOT_FOUND;

/*
 Self-Invocation을 피하기 위한 outbox state updater
 */
@Component
@RequiredArgsConstructor
public class OutboxStateUpdater {

    private final EventOutboxRepository eventOutboxRepository;

    @Transactional( propagation = Propagation.REQUIRES_NEW )
    public void recordEventSuccess( AggregateEvent event ) {
        OutboxMessage outboxMessage = findByEvent( event );
        outboxMessage.recordSuccess();
    }

    @Transactional( propagation = Propagation.REQUIRES_NEW )
    public void recordEventFail( AggregateEvent event ) {
        OutboxMessage outboxMessage = findByEvent( event );
        outboxMessage.recordFailure();
    }

    private OutboxMessage findByEvent( AggregateEvent event) {
        return eventOutboxRepository.findByEvent(
                event.getEventType(),
                event.getEventStatus(),
                event.getEventDate(),
                event.getAggregateId()
        ).orElseThrow( () -> new EventException( EVENT_NOT_FOUND ) );
    }

}
