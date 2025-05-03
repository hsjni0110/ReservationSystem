package com.example.reservationsystem.common.infra.publisher.impl;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.infra.publisher.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Qualifier("application")
@Slf4j
public class ApplicationEventPublisherImpl implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish( AggregateEvent event ) {
        log.info("📣 Publishing event: {}", event.getClass().getSimpleName());
        applicationEventPublisher.publishEvent( event );
    }

}
