package com.example.reservationsystem.payment.application.processors;

import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.common.application.EventOutboxService;
import com.example.reservationsystem.common.domain.MessageProcessingService;
import com.example.reservationsystem.common.infra.publisher.DomainEventPublisher;
import com.example.reservationsystem.payment.application.PaymentService;
import com.example.reservationsystem.payment.application.dto.CompletedPaymentResponse;
import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDebitedEventProcessor {

    private final MessageProcessingService processor;
    private final PaymentService paymentService;
    private final DomainEventPublisher eventPublisher;

    public void process(AccountDebitedEvent event, String eventIdStr) {
        processor.process(
                eventIdStr,
                event,
                e -> {
                    CompletedPaymentResponse result = paymentService.successPayment(e.userId(), e.reservationId());
                    eventPublisher.publish(new PaymentSuccessEvent(result.paymentId(), e.userId(), e.reservationId(), result.totalPrice()));
                },
                (e, ex) -> {
                },
                false
        );
    }
}
