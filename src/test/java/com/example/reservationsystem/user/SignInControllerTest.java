package com.example.reservationsystem.user;

import com.example.reservationsystem.common.ControllerTest;
import com.example.reservationsystem.user.signin.application.SignInService;
import com.example.reservationsystem.user.signin.dto.SignInRequest;
import com.example.reservationsystem.user.signin.dto.SignInResponse;
import com.example.reservationsystem.user.signin.presentation.SignInController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
