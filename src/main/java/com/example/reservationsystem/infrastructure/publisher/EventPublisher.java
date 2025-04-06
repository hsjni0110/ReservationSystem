package com.example.reservationsystem.infrastructure.publisher;

import com.example.reservationsystem.common.domain.AggregateEvent;

public interface EventPublisher {

    void publishEvent( AggregateEvent event );

}
