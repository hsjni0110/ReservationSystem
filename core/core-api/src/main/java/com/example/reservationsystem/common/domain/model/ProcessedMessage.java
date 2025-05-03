package com.example.reservationsystem.common.domain.model;

import com.example.reservationsystem.common.type.ConsumerType;
import com.example.reservationsystem.common.type.ProcessedStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "PROCESSED_MESSAGE" )
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedMessage {

    @EmbeddedId
    private ProcessedMessageId id;

    @Enumerated( EnumType.STRING )
    @Column( name = "status", nullable = false )
    private ProcessedStatus status;

    @Column( name = "processed_at", nullable = false )
    private LocalDateTime processedAt;

    public static ProcessedMessage success( Long messageId, ConsumerType consumerType ) {
        return ProcessedMessage.builder()
                .id( new ProcessedMessageId( messageId, consumerType ) )
                .status( ProcessedStatus.SUCCESS )
                .processedAt( LocalDateTime.now() )
                .build();
    }

    public static ProcessedMessage skip(Long messageId, ConsumerType consumerType) {
        return ProcessedMessage.builder()
                .id( new ProcessedMessageId( messageId, consumerType) )
                .status( ProcessedStatus.SKIPPED )
                .processedAt( LocalDateTime.now() )
                .build();
    }

}