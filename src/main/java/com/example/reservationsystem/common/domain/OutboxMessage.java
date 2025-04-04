package com.example.reservationsystem.common.domain;

import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private Long outboxMessageId;

    @Enumerated(EnumType.STRING)
    @Column( name = "aggregate_type", nullable = false )
    private AggregateType aggregateType;

    @Column( name = "aggregate_id", length = 100, nullable = false )
    private Long aggregateId;

    @Enumerated(EnumType.STRING)
    @Column( name = "event_type", length = 100, nullable = false )
    private EventType eventType;

    @Column( name = "payload", columnDefinition = "json", nullable = false )
    private String payload;

    @Enumerated( EnumType.STRING )
    @Column( name = "event_status", nullable = false)
    private EventStatus eventStatus;

    @Column( name = "created_at", nullable = false, updatable = false )
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column( name = "retry_count", nullable = false )
    private int retryCount;

}
