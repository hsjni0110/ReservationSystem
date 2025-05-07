package com.system.application.processors;

import com.system.application.PaymentService;
import com.system.application.dto.CompletedPaymentResponse;
import com.system.domain.MessageProcessingService;
import com.system.domain.event.AccountDebitedEvent;
import com.system.domain.event.PaymentSuccessEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.system.type.ConsumerType;

@Service
@RequiredArgsConstructor
public class AccountDebitedEventProcessor {

    private final MessageProcessingService processor;
    private final PaymentService paymentService;
    private final com.system.publisher.DomainEventPublisher eventPublisher;

    @Transactional
    public void process(AccountDebitedEvent event, String eventIdStr ) {
        processor.process(
                eventIdStr,
                event,
                ConsumerType.CONFIRM_SUCCESS_PAYMENT,
                e -> {
                    CompletedPaymentResponse result = paymentService.successPayment( e.userId(), e.reservationId() );
                    eventPublisher.publish( new PaymentSuccessEvent(result.paymentId(), e.userId(), e.reservationId(), result.totalPrice()) );
                },
                (e, ex) -> {
                },
                false
        );
    }
}
