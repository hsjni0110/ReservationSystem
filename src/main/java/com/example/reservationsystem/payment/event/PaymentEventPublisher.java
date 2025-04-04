package com.example.reservationsystem.payment.event;

public interface PaymentEventPublisher {

    void publishPaymentAttemptEvent(PaymentAttemptEvent event);

}
