package com.system.domain.repository;

import com.system.domain.ProcessedMessage;
import com.system.domain.ProcessedMessageId;
import com.system.type.ConsumerType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage, Long> {

    boolean existsById( ProcessedMessageId id );

    @Query("SELECT pm.id.consumerType FROM ProcessedMessage pm WHERE pm.id.processedMessageId = :messageId")
    List<ConsumerType> findAllConsumerTypesByMessageId(@Param("messageId") Long messageId);

}
