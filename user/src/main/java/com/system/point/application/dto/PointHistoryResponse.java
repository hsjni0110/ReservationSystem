package com.system.point.application.dto;

import com.system.domain.Money;
import java.time.LocalDateTime;

public record PointHistoryResponse(
        Long historyId,
        Long userId,
        Money earnedPoints,
        String description,
        LocalDateTime createdAt
) {
}