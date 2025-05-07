package com.system.domain;

import com.system.type.ConsumerType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProcessedMessageId implements Serializable {

    @Column(name = "processed_message_id", nullable = false)
    private Long processedMessageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "consumer_type", nullable = false, length = 100)
    private ConsumerType consumerType;

}
