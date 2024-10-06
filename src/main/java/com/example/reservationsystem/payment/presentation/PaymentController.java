package com.example.reservationsystem.payment.presentation;

import com.example.reservationsystem.auth.Auth;
import com.example.reservationsystem.payment.application.PaymentService;
import com.example.reservationsystem.payment.dto.PaymentRequest;
import com.example.reservationsystem.payment.dto.PaymentResponse;
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

}
