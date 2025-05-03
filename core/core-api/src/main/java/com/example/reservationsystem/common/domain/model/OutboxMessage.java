package com.example.reservationsystem.common.domain.model;

import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "OUTBOX_MESSAGE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxMessage {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Getter
    private Long outboxMessageId;

    @Enumerated(EnumType.STRING)
    @Column( name = "aggregate_type", nullable = false )
    private AggregateType aggregateType;

    @Column( name = "aggregate_id", length = 100, nullable = false )
    private Long aggregateId;

    @Enumerated(EnumType.STRING)
    @Column( name = "event_type", length = 100, nullable = false )
    @Getter
    private EventType eventType;

    @Column( name = "payload", columnDefinition = "json", nullable = false )
    @Getter
    private String payload;

    @Enumerated( EnumType.STRING )
    @Column( name = "event_status", nullable = false)
    @Getter
    private EventStatus eventStatus;

    @Column( name = "event_date", nullable = false, updatable = false )
    private LocalDateTime eventDate;

    @Column( name = "retry_count", nullable = false )
    private int retryCount;

    public void recordSuccess() {
        this.eventStatus = EventStatus.SEND_SUCCESS;
    }

    public void recordFailure() {
        this.eventStatus = EventStatus.SEND_FAILURE;
    }

    public boolean isProcessedEvent() {
        return this.eventStatus == EventStatus.SEND_SUCCESS || this.eventStatus == EventStatus.SEND_FAILURE;
    }

}
