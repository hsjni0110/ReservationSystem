package com.reservationsystem.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservationsystem.support.ControllerTest;
import com.system.application.AccountService;
import com.system.application.dto.BalanceRequest;
import com.system.reservationsystem.controller.AccountController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        willDoNothing().given(accountService).recharge(any(Long.class), any(Long.class));

        // when, then
        mockMvc.perform(post("/account/recharge")
                .content(objectMapper.writeValueAsString(balanceRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
