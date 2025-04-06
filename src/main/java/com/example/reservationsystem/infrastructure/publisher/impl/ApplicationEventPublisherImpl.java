package com.example.reservationsystem.infrastructure.publisher.impl;

import com.example.reservationsystem.common.domain.AggregateEvent;
import com.example.reservationsystem.infrastructure.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Qualifier("application")
public class ApplicationEventPublisherImpl implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent( AggregateEvent event ) {
        applicationEventPublisher.publishEvent( event );
    }

}
