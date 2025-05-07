package com.system.domain;

import com.system.domain.repository.ProcessedMessageRepository;
import com.system.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.system.type.ConsumerType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProcessingService {

    private final ProcessedMessageRepository processedMessageRepository;
    private final FailureHandler failureHandler;

    public <T extends AggregateEvent> void process(
            String eventIdStr,
            T event,
            ConsumerType consumerType,
            Consumer<T> onSuccess,
            BiConsumer<T, BaseException> onFailure,
            boolean shouldSkipOnFailure
    ) {
        Long eventId = Long.valueOf(eventIdStr);

        if ( processedMessageRepository.existsById( new ProcessedMessageId( eventId, consumerType ) ) ) {
            log.info( "[Kafka] Skip already processed eventId={}", eventId );
            return;
        }

        try {
            onSuccess.accept(event);
            processedMessageRepository.save( ProcessedMessage.success( eventId, consumerType ) );

        } catch (BaseException e) {
            log.error("[Kafka] handle failed - eventId={}, error={}", eventId, e.exceptionType().errorMessage(), e);
            failureHandler.handleFailure( eventId, event, consumerType, ev -> onFailure.accept( ev, e ), shouldSkipOnFailure );
            throw e;
        }

    }

}