package com.example.reservationsystem.common.domain;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.domain.model.ProcessedMessage;
import com.example.reservationsystem.common.exception.BaseException;
import com.example.reservationsystem.common.infra.repository.ProcessedMessageRepository;
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
            BaseException cause,
            Consumer<T> failureLogic,
            boolean shouldSkip
    ) {
        if ( shouldSkip ) {
            processedMessageRepository.save(ProcessedMessage.skip(eventId));
        }
        failureLogic.accept(event);
    }

}
