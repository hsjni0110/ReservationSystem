package com.example.reservationsystem.common.infra.publisher;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface ExternalEventPublisher {

    CompletableFuture<SendResult<String, Object>> publish(AggregateEvent event, Long outboxMessageId );

}
