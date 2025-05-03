package com.example.reservationsystem.common.application;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.domain.model.OutboxMessage;
import com.example.reservationsystem.common.domain.model.OutboxStateUpdater;
import com.example.reservationsystem.common.infra.publisher.ExternalEventPublisher;
import com.example.reservationsystem.common.infra.repository.EventOutboxRepository;
import com.example.reservationsystem.common.exception.EventException;
import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.reservationsystem.common.exception.EventExceptionType.EVENT_NOT_FOUND;
import static com.example.reservationsystem.common.utils.JsonUtils.toJson;

@Service
@RequiredArgsConstructor
public class EventOutboxService {

    private final ExternalEventPublisher eventPublisher;
    private final OutboxEventMapper outboxEventMapper;
    private final EventOutboxRepository eventOutboxRepository;
    private final OutboxStateUpdater outboxStateUpdater;

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
        eventPublisher.publish(event, findByEvent( event ).getOutboxMessageId() )
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        outboxStateUpdater.recordEventFail(event);
                    } else {
                        outboxStateUpdater.recordEventSuccess(event);
                    }
                });
    }

    public OutboxMessage findByEvent( AggregateEvent event) {
        return eventOutboxRepository.findByEvent(
                event.getEventType(),
                event.getEventStatus(),
                event.getEventDate(),
                event.getAggregateId()
        ).orElseThrow( () -> new EventException( EVENT_NOT_FOUND ) );
    }

    @Transactional
    public void retryUnprocessedEvents() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);

        List<OutboxMessage> unprocessed = eventOutboxRepository
                .findAllByStatusNotAndBeforeDate(EventStatus.SEND_SUCCESS, threshold);

        for (OutboxMessage message : unprocessed) {
            AggregateEvent event = outboxEventMapper.toEventObject(
                    message.getPayload(),
                    message.getEventType()
            );
            publishEvent( event );
        }
    }

    @Transactional
    public void deleteOldPublishedEvents() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        eventOutboxRepository.deleteAllByStatusBeforeDate(EventStatus.SEND_SUCCESS, threshold);
    }

}
