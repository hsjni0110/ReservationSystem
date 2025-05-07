package com.system.domain;

import com.system.domain.repository.ProcessedMessageRepository;
import com.system.type.ConsumerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class FailureHandler {

    private final ProcessedMessageRepository processedMessageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T extends AggregateEvent> void handleFailure(
            Long eventId,
            T event,
            ConsumerType consumerType,
            Consumer<T> failureLogic,
            boolean shouldSkip
    ) {
        if ( shouldSkip ) {
            processedMessageRepository.save( ProcessedMessage.skip( eventId, consumerType ));
        }
        failureLogic.accept(event);
    }

}
