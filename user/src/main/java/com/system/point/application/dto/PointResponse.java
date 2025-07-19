package com.system.point.application.dto;

import com.system.domain.Money;

public record PointResponse(
        Long pointId,
        Long userId,
        Money totalPoints
) {
}