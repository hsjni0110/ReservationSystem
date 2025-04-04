package com.example.reservationsystem.infrastructure.publisher;

import com.example.reservationsystem.payment.event.PaymentAttemptEvent;
import com.example.reservationsystem.payment.event.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Qualifier("application")
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishPaymentAttemptEvent(PaymentAttemptEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

}
