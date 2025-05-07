package com.system.scheduler;

import com.system.application.EventOutboxService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final EventOutboxService eventOutboxService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 실패한 이벤트 재발행 스케줄링
     */
    @Scheduled(fixedRate = 60000)
    public void retryFailedEvents() {
        logger.info("Retry Failed Outbox Event Scheduler Executed");
        eventOutboxService.retryUnprocessedEvents();
    }

    /**
     * 오래된 성공 이벤트 삭제 스케줄링
     */
    @Scheduled(fixedRate = 60000)
    public void deleteOldPublishedEvents() {
        logger.info("Delete Published Outbox Event Scheduler Executed");
        eventOutboxService.deleteOldPublishedEvents();
    }

}
