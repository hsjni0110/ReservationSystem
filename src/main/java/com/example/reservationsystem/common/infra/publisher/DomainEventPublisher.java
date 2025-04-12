package com.example.reservationsystem.common.infra.publisher;

import com.example.reservationsystem.common.domain.model.AggregateEvent;

public interface DomainEventPublisher {

    void publish(AggregateEvent event );

}
