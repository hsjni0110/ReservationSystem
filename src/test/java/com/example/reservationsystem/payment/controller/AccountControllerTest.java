package com.example.reservationsystem.payment.controller;

import com.example.reservationsystem.account.application.AccountService;
import com.example.reservationsystem.account.controller.AccountController;
import com.example.reservationsystem.account.dto.BalanceRequest;
import com.example.reservationsystem.account.dto.BalanceResponse;
import com.example.reservationsystem.common.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("계좌 컨트롤러(AccountController)는")
@WebMvcTest(AccountController.class)
public class AccountControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        사용자_인가();
    }

    @Test
    void 금액을_충전할_수_있다() throws Exception {
        // given
        long rechargeAmount = 10000L;
        BalanceRequest balanceRequest = new BalanceRequest(10000L);
        BalanceResponse balanceResponse = new BalanceResponse(20000L);
        given(accountService.recharge(any(Long.class), any(Long.class))).willReturn(balanceResponse);

        // when, then
        mockMvc.perform(post("/account/recharge")
                .content(objectMapper.writeValueAsString(balanceRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(20000L));
    }

}
