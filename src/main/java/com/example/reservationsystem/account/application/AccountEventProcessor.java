package com.example.reservationsystem.account.application;

import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.common.application.EventOutboxService;
import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.infra.publisher.EventPublisher;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountEventProcessor {

    private final AccountService accountService;
    private final EventOutboxService eventOutboxService;
    private final EventPublisher eventPublisher;

    public AccountEventProcessor( AccountService accountService, EventOutboxService eventOutboxService, @Qualifier("application") EventPublisher eventPublisher ) {
        this.accountService = accountService;
        this.eventOutboxService = eventOutboxService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handlePaymentAttempt( PaymentAttemptEvent event ) {
        Long accountId = accountService.debit(event.userId(), event.totalPrice().longValue());
        eventOutboxService.recordEventSuccess( event );
        eventPublisher.publishEvent( new AccountDebitedEvent( accountId, event.userId(), event.reservationId() ) );
    }

    public boolean isDuplicate( PaymentAttemptEvent event ) {
        return eventOutboxService.checkDuplicateEvent( event );
    }

    public void markFailure( AggregateEvent event ) {
        eventOutboxService.recordEventFailure( event );
    }

}
