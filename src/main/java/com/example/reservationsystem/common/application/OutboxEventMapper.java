package com.example.reservationsystem.common.application;

import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.type.EventType;
import com.example.reservationsystem.common.utils.JsonUtils;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventMapper {

    public AggregateEvent toEventObject(String payload, EventType eventType ) {
        return switch (eventType) {
            case PAYMENT_ATTEMPT -> JsonUtils.fromJson(payload, PaymentAttemptEvent.class);
            case PAYMENT_SUCCESS -> JsonUtils.fromJson(payload, PaymentSuccessEvent.class);
            case ACCOUNT_DEBITED -> JsonUtils.fromJson(payload, AccountDebitedEvent.class);
            case ACCOUNT_DEBITED_FAILURE -> JsonUtils.fromJson(payload, AccountDebitFailedEvent.class);
        };
    }

}
