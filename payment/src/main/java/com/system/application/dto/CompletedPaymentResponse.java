package com.system.application.dto;

public record CompletedPaymentResponse(
        Long paymentId,
        Long totalPrice
) {
}
