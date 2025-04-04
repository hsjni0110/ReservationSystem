package com.example.reservationsystem.infrastructure.event;

import com.example.reservationsystem.common.domain.EventOutboxService;
import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;
import com.example.reservationsystem.payment.event.PaymentAttemptEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final EventOutboxService eventOutboxService;

    @TransactionalEventListener( phase = TransactionPhase.BEFORE_COMMIT )
    public void saveEventOutBoxForPaymentAttempted( PaymentAttemptEvent paymentAttemptEvent ) {
        eventOutboxService.save(
                AggregateType.PAYMENT,
                paymentAttemptEvent,
                paymentAttemptEvent.getAggregateId(),
                EventType.PAYMENT_ATTEMPT,
                EventStatus.INIT
        );
    }


}
