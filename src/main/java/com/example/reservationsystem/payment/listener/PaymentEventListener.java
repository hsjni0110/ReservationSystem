package com.example.reservationsystem.payment.listener;

import com.example.reservationsystem.common.application.EventOutboxService;
import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final EventOutboxService eventOutboxService;

    @TransactionalEventListener( phase = TransactionPhase.BEFORE_COMMIT )
    public void saveEventOutBoxForPaymentAttempted( PaymentAttemptEvent paymentAttemptEvent ) {
        eventOutboxService.save(
                AggregateType.PAYMENT,
                paymentAttemptEvent,
                paymentAttemptEvent.getAggregateId(),
                paymentAttemptEvent.getEventType(),
                paymentAttemptEvent.createdAt(),
                EventStatus.INIT
        );
    }

    @TransactionalEventListener( phase = TransactionPhase.AFTER_COMMIT )
    public void sendPaymentAttemptEvent( PaymentAttemptEvent paymentAttemptEvent ) {
        eventOutboxService.publishEvent( paymentAttemptEvent );
    }

    @TransactionalEventListener( phase = TransactionPhase.BEFORE_COMMIT )
    public void saveEventOutBoxForPaymentSuccess( PaymentSuccessEvent paymentSuccessEvent ) {
        eventOutboxService.save(
                AggregateType.PAYMENT,
                paymentSuccessEvent,
                paymentSuccessEvent.getAggregateId(),
                paymentSuccessEvent.getEventType(),
                paymentSuccessEvent.getEventDate(),
                EventStatus.INIT
        );
    }

    @TransactionalEventListener( phase = TransactionPhase.AFTER_COMMIT )
    public void sendPaymentSuccessEvent( PaymentSuccessEvent paymentSuccessEvent ) {
        eventOutboxService.publishEvent( paymentSuccessEvent );
    }

}
