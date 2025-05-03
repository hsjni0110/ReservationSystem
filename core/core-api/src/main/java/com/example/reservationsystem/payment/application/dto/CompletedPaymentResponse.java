package com.example.reservationsystem.payment.application.dto;

public record CompletedPaymentResponse(
        Long paymentId,
        Long totalPrice
) {
}
