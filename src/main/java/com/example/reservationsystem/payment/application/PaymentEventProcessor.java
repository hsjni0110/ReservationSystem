package com.example.reservationsystem.payment.application;

import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.common.application.EventOutboxService;
import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.infra.publisher.EventPublisher;
import com.example.reservationsystem.payment.application.dto.CompletedPaymentResponse;
import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProcessor {

    private final EventOutboxService eventOutboxService;
    private final PaymentService paymentService;
    private final EventPublisher eventPublisher;

    public PaymentEventProcessor( EventOutboxService eventOutboxService, PaymentService paymentService, @Qualifier("application") EventPublisher eventPublisher ) {
        this.eventOutboxService = eventOutboxService;
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handleAccountDebited( AccountDebitedEvent event ) {
        CompletedPaymentResponse completedPaymentResponse = paymentService.successPayment(event.userId(), event.reservationId());
        eventOutboxService.recordEventSuccess( event );
        eventPublisher.publishEvent( new PaymentSuccessEvent( completedPaymentResponse.paymentId(), event.userId(), event.reservationId(), completedPaymentResponse.totalPrice()) );
    }

    public void markFailure( AggregateEvent event ) {
        eventOutboxService.recordEventFailure( event );
    }

    @Transactional
    public void handleInsufficientAmount( AccountDebitFailedEvent event ) {
        paymentService.cancelPayment( event.userId(), event.reservationId() );
    }

}
