package com.reservation.reservationsystem.user.controller;

import com.reservation.reservationsystem.common.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.reservationsystem.controller.SignInController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.system.user.signin.application.SignInService;
import com.system.user.signin.application.dto.SignInRequest;
import com.system.user.signin.application.dto.SignInResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 컨트롤러(SignInController)는 ")
@WebMvcTest(SignInController.class)
public class SignInControllerTest extends ControllerTest{

    @MockBean
    private SignInService signInService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 유저는_로그인을_할_수_있다() throws Exception{
        SignInRequest signInRequest = new SignInRequest("hanhan@naver.com", "12345");
        given(signInService.signIn(any(String.class), any(String.class))).willReturn(new SignInResponse("accessToken"));

        mockMvc.perform(post("/sign-in")
                .content(objectMapper.writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty());
    }

}
