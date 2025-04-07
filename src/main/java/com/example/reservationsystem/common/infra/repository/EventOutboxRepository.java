package com.example.reservationsystem.common.infra.repository;

import com.example.reservationsystem.common.domain.model.OutboxMessage;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventOutboxRepository extends JpaRepository<OutboxMessage, Long> {

    @Query("""
    SELECT m FROM OutboxMessage m
    WHERE m.eventType = :eventType
      AND m.eventStatus = :eventStatus
      AND m.eventDate = :eventDate
      AND m.aggregateId = :aggregateId
    """)
    Optional<OutboxMessage> findByEvent(
            @Param("eventType") EventType eventType,
            @Param("eventStatus") EventStatus eventStatus,
            @Param("eventDate") LocalDateTime eventDate,
            @Param("aggregateId") Long aggregateId
    );

    @Query("""
    SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END
    FROM OutboxMessage m
    WHERE m.eventType = :eventType
      AND m.eventStatus = :eventStatus
      AND m.eventDate = :eventDate
      AND m.aggregateId = :aggregateId
""")
    boolean existsByEvent(
            @Param("eventType") EventType eventType,
            @Param("eventStatus") EventStatus eventStatus,
            @Param("eventDate") LocalDateTime eventDate,
            @Param("aggregateId") Long aggregateId
    );

}
