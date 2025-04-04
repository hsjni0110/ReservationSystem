package com.example.reservationsystem.common.domain;

import com.example.reservationsystem.common.domain.repository.EventOutboxRepository;
import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventOutboxService {

    private final EventOutboxRepository eventOutboxRepository;

    public void save( AggregateType aggregateType, AggregateEvent aggregateEvent, Long aggregateId, EventType eventType, EventStatus eventStatus ) {
        OutboxMessage message = OutboxMessage.builder()
                .aggregateType( aggregateType )
                .aggregateId( aggregateId )
                .payload( aggregateEvent.toString() )
                .eventType( eventType )
                .eventStatus( eventStatus )
                .retryCount( 0 )
                .build();

        eventOutboxRepository.save( message );
    }

}
