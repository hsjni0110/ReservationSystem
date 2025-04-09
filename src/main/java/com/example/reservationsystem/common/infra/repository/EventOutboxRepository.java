package com.example.reservationsystem.common.infra.repository;

import com.example.reservationsystem.common.domain.model.OutboxMessage;
import com.example.reservationsystem.common.type.EventStatus;
import com.example.reservationsystem.common.type.EventType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
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
    SELECT m FROM OutboxMessage m
    WHERE m.eventType = :eventType
      AND m.eventDate = :eventDate
      AND m.aggregateId = :aggregateId
    """)
    Optional<OutboxMessage> findByEventExceptStatus(
            @Param("eventType") EventType eventType,
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

    @Query("SELECT om FROM OutboxMessage om WHERE om.eventStatus = :status AND om.eventDate < :dateTime")
    List<OutboxMessage> findAllByStatusBeforeDate(@Param("status") EventStatus status, @Param("dateTime") LocalDateTime dateTime);

    @Modifying
    @Query("DELETE FROM OutboxMessage om WHERE om.eventStatus = :status AND om.eventDate < :dateTime")
    void deleteAllByStatusBeforeDate(@Param("status") EventStatus status, @Param("dateTime") LocalDateTime dateTime);

}
