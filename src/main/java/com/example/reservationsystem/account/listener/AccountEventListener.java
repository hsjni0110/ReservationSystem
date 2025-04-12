package com.example.reservationsystem.account.listener;

import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.common.application.EventOutboxService;
import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventListener {

    private final EventOutboxService eventOutboxService;

    @TransactionalEventListener( phase = TransactionPhase.BEFORE_COMMIT )
    public void saveEventOutBoxForAccountDebited( AccountDebitedEvent accountDebitedEvent ) {
        eventOutboxService.save(
                AggregateType.ACCOUNT,
                accountDebitedEvent,
                accountDebitedEvent.getAggregateId(),
                accountDebitedEvent.getEventType(),
                accountDebitedEvent.getEventDate(),
                EventStatus.INIT
        );
    }

    @Async
    @TransactionalEventListener( phase = TransactionPhase.AFTER_COMMIT )
    public void sendAccountDebitedEvent( AccountDebitedEvent accountDebitedEvent ) {
        eventOutboxService.publishEvent( accountDebitedEvent );
    }

    @TransactionalEventListener( phase = TransactionPhase.BEFORE_COMMIT )
    public void saveEventOutBoxForInsufficientAmount( AccountDebitFailedEvent event ) {
        eventOutboxService.save(
                AggregateType.ACCOUNT,
                event,
                event.getAggregateId(),
                event.getEventType(),
                event.getEventDate(),
                EventStatus.INIT
        );
    }

    @Async
    @TransactionalEventListener( phase = TransactionPhase.AFTER_COMMIT )
    public void sendInsufficientAmountEvent( AccountDebitFailedEvent event ) {
        log.info("✅ AFTER_COMMIT caught event: {}", event.getClass().getSimpleName());
        eventOutboxService.publishEvent( event );
    }

}