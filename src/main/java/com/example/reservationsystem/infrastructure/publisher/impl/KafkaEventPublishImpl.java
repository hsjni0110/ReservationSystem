package com.example.reservationsystem.infrastructure.publisher.impl;

import com.example.reservationsystem.common.domain.AggregateEvent;
import com.example.reservationsystem.infrastructure.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.example.reservationsystem.common.utils.JsonUtils.toJson;

@Component
@RequiredArgsConstructor
@Qualifier("kafka")
public class KafkaEventPublishImpl implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishEvent( AggregateEvent event ) {
        kafkaTemplate
                .send( event.getEventType().toString(), toJson( event ) );
    }

}
