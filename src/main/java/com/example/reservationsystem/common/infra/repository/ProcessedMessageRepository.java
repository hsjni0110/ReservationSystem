package com.example.reservationsystem.common.infra.repository;

import com.example.reservationsystem.common.domain.model.ProcessedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage, Long> {
}
