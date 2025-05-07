package com.system.application.dto;

import com.system.type.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        BigDecimal payPrice,
        PaymentStatus paymentStatus,
        LocalDateTime createdDate
) {
}
