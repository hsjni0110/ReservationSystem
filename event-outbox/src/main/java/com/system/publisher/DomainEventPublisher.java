package com.system.publisher;

import com.system.domain.AggregateEvent;

public interface DomainEventPublisher {

    void publish( AggregateEvent event );

}
