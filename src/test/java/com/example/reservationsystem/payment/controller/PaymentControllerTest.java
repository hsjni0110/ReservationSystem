package com.example.reservationsystem.payment.controller;

import com.example.reservationsystem.common.ControllerTest;
import com.example.reservationsystem.common.type.PaymentStatus;
import com.example.reservationsystem.payment.application.PaymentService;
import com.example.reservationsystem.payment.dto.PaymentRequest;
import com.example.reservationsystem.payment.dto.PaymentResponse;
import com.example.reservationsystem.payment.presentation.PaymentController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("PaymentController는")
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest extends ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        사용자_인가();
    }

    @Test
    void reservationId와_user가_인가되면_결제를_할_수_있다() throws Exception {
        // given
        Long reservationId = 1L;
        Long userId = 1L;
        Long paymentId = 1L;
        BigDecimal payPrice = BigDecimal.valueOf(5000);
        PaymentStatus payStatus = PaymentStatus.PAYED;
        LocalDateTime createdDate = LocalDateTime.now();

        PaymentRequest paymentRequest = new PaymentRequest(reservationId);
        PaymentResponse paymentResponse = new PaymentResponse(paymentId, payPrice, payStatus, createdDate);

        given(paymentService.pay(userId, reservationId)).willReturn(paymentResponse);

        // when & then
        mockMvc.perform(post("/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(paymentId))
                .andExpect(jsonPath("$.payPrice").value(payPrice))
                .andExpect(jsonPath("$.paymentStatus").value(payStatus.name()))
                .andExpect(jsonPath("$.createdDate").isNotEmpty());
    }
}