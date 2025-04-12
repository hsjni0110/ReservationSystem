package com.example.reservationsystem.common.domain.model;

import com.example.reservationsystem.common.type.AggregateType;
import com.example.reservationsystem.common.type.EventType;
import com.example.reservationsystem.common.type.ProcessedStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROCESSED_MESSAGE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedMessage {

    @Id
    @Column( nullable = false )
    private Long processedMessageId;

    @Enumerated( EnumType.STRING )
    @Column( name = "status", nullable = false )
    private ProcessedStatus status;

    @Column( name = "processed_at", nullable = false )
    private LocalDateTime processedAt;

    public static ProcessedMessage success(Long id) {
        return ProcessedMessage.builder()
                .processedMessageId(id)
                .status(ProcessedStatus.SUCCESS)
                .processedAt(LocalDateTime.now())
                .build();
    }

    public static ProcessedMessage skip(Long id) {
        return ProcessedMessage.builder()
                .processedMessageId(id)
                .status(ProcessedStatus.SKIPPED)
                .processedAt(LocalDateTime.now())
                .build();
    }

}