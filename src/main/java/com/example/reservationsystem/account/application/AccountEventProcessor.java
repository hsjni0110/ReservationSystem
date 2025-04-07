package com.example.reservationsystem.account.application;

import com.example.reservationsystem.common.domain.EventOutboxService;
import com.example.reservationsystem.payment.event.PaymentAttemptEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventProcessor {

    private final AccountService accountService;
    private final EventOutboxService eventOutboxService;

    @Transactional
    public void handlePaymentAttempt( PaymentAttemptEvent event ) {
        accountService.debit( event.userId(), event.totalPrice().longValue() );
        eventOutboxService.recordEventSuccess( event );
    }

    public boolean isDuplicate( PaymentAttemptEvent event ) {
        return eventOutboxService.checkDuplicateEvent( event );
    }

    public void markFailure( PaymentAttemptEvent event ) {
        eventOutboxService.recordEventFailure( event );
    }

}
