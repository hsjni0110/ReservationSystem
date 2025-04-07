package com.example.reservationsystem.account.listener;

import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.common.application.EventOutboxService;
import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
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

    @TransactionalEventListener( phase = TransactionPhase.AFTER_COMMIT )
    public void sendAccountDebitedEvent( AccountDebitedEvent accountDebitedEvent ) {
        eventOutboxService.publishEvent( accountDebitedEvent );
    }

}
