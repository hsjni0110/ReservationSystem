package com.system.application;

import com.system.domain.MessageProcessingService;
import com.system.domain.event.AccountDebitFailedEvent;
import com.system.domain.event.AccountDebitedEvent;
import com.system.domain.event.PaymentAttemptEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.system.publisher.DomainEventPublisher;
import com.system.type.ConsumerType;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountEventProcessor {

    private final MessageProcessingService processor;
    private final AccountService accountService;
    private final DomainEventPublisher eventPublisher;

    /*
     성공 시에는 AccountDebitedEvent 이벤트 발행
     실패 시에는 AccountDebitFailedEvent 이벤트 발행
     실패 시의 다음 스킵 여부 전달.(반드시 재처리가 되어야 하는 경우) true면 재처리 필요 없이 롤백
     */
    @Transactional
    public void process( PaymentAttemptEvent event, String eventIdStr ) {
        processor.process(
                eventIdStr,
                event,
                ConsumerType.ACCOUNT_DEBIT,
                e -> {
                    Long accountId = accountService.debit( e.userId(), e.totalPrice().longValue() );
                    eventPublisher.publish( new AccountDebitedEvent( accountId, e.userId(), e.reservationId() ) );
                },
                (e, ex) -> {
                    eventPublisher.publish( new AccountDebitFailedEvent( e.userId(), e.reservationId() ) );
                },
                true
        );
    }

}
