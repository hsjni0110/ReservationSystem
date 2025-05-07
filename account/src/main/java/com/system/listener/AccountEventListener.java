package com.system.listener;

import com.system.application.AccountService;
import com.system.application.EventOutboxService;
import com.system.domain.event.AccountDebitFailedEvent;
import com.system.domain.event.AccountDebitedEvent;
import com.system.domain.event.UserSignUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import com.system.type.AggregateType;
import com.system.type.EventStatus;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventListener {

    private final EventOutboxService eventOutboxService;
    private final AccountService accountService;

    @EventListener
    public void userSignUpEvent( UserSignUpEvent event ) {
        accountService.createAccount( event.userId() );
    }

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