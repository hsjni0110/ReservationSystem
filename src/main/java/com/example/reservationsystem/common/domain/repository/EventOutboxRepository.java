package com.example.reservationsystem.common.domain.repository;

import com.example.reservationsystem.common.domain.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventOutboxRepository extends JpaRepository<OutboxMessage, Long> {
}
