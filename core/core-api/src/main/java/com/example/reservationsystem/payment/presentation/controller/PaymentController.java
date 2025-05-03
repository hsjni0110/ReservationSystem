package com.example.reservationsystem.payment.presentation.controller;

import com.example.reservationsystem.auth.domain.Auth;
import com.example.reservationsystem.payment.application.PaymentService;
import com.example.reservationsystem.payment.application.dto.PaymentRequest;
import com.example.reservationsystem.payment.application.dto.PaymentResponse;
import com.example.reservationsystem.payment.application.dto.PaymentStatusResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> pay(
            @Auth Long userId,
            @RequestBody PaymentRequest request
    ) {
        PaymentResponse paymentResponse = paymentService.pay(userId, request.reservationId());
        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping
    public ResponseEntity<PaymentStatusResponse> getPaymentStatus(
            @Auth Long userId,
            @RequestBody PaymentRequest request
    ) {
        PaymentStatusResponse paymentStatus = paymentService.getPaymentStatus(userId, request.reservationId());
        return ResponseEntity.ok(paymentStatus);
    }

}
