package com.example.reservationsystem.payment.dto;

import com.example.reservationsystem.common.type.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        BigDecimal payPrice,
        PaymentStatus paymentStatus,
        LocalDateTime createdDate
) {
}
