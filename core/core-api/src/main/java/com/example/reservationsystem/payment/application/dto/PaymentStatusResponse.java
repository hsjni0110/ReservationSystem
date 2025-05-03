package com.example.reservationsystem.payment.application.dto;

import com.example.reservationsystem.common.type.PaymentStatus;

public record PaymentStatusResponse(
        PaymentStatus paymentStatus
) {
}
