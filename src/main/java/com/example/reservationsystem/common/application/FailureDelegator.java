package com.example.reservationsystem.common.application;

import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.domain.model.ProcessedMessage;
import com.example.reservationsystem.common.exception.BaseException;
import com.example.reservationsystem.common.infra.repository.ProcessedMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FailureDelegator {

    private final ProcessedMessageRepository processedMessageRepository;

    @Transactional
    public void handleFailure(Long eventId, AggregateEvent event, BaseException cause, Runnable domainFailureLogic) {
        processedMessageRepository.save(ProcessedMessage.skip(eventId));
        domainFailureLogic.run();
    }

}
