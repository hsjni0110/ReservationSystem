package com.system.publisher;

import com.system.domain.AggregateEvent;
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
