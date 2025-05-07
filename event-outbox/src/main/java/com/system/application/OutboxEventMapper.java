package com.system.application;

import com.system.domain.AggregateEvent;
import com.system.domain.event.AccountDebitFailedEvent;
import com.system.domain.event.AccountDebitedEvent;
import com.system.domain.event.PaymentAttemptEvent;
import com.system.domain.event.PaymentSuccessEvent;
import org.springframework.stereotype.Component;
import com.system.type.EventType;
import com.system.utils.JsonUtils;

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
