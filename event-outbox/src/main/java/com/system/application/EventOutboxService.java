package com.system.application;

import com.system.application.exception.EventException;
import com.system.domain.AggregateEvent;
import com.system.domain.OutboxMessage;
import com.system.domain.OutboxStateUpdater;
import com.system.domain.repository.EventOutboxRepository;
import com.system.publisher.ExternalEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.system.type.AggregateType;
import com.system.type.EventStatus;
import com.system.type.EventType;

import java.time.LocalDateTime;
import java.util.List;

import static com.system.application.exception.EventExceptionType.EVENT_NOT_FOUND;
import static com.system.utils.JsonUtils.toJson;

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
