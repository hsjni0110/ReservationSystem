package com.system.reservationsystem.controller;

import com.system.application.PaymentService;
import com.system.application.dto.PaymentRequest;
import com.system.application.dto.PaymentResponse;
import com.system.application.dto.PaymentStatusResponse;
import com.system.auth.domain.Auth;
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
