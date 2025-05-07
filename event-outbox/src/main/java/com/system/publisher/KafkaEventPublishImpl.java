package com.system.publisher;

import com.system.domain.AggregateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.system.utils.JsonUtils.toJson;

@Component
@RequiredArgsConstructor
@Qualifier("kafka")
@Slf4j
public class KafkaEventPublishImpl implements ExternalEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public CompletableFuture<SendResult<String, Object>> publish( AggregateEvent event, Long outboxMessageId) {
        ProducerRecord<String, Object> record = buildKafkaRecord(event, outboxMessageId);
        return kafkaTemplate.send(record);
    }

    private ProducerRecord<String, Object> buildKafkaRecord( AggregateEvent event, Long eventId ) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                event.getEventType().toString(),
                null,
                null,
                null,
                toJson( event )
        );

        record.headers().add( "eventId", Long.toString(eventId).getBytes() );
        return record;
    }

}
