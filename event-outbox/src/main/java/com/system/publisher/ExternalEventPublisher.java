package com.system.publisher;

import com.system.domain.AggregateEvent;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface ExternalEventPublisher {

    CompletableFuture<SendResult<String, Object>> publish( AggregateEvent event, Long outboxMessageId );

}
