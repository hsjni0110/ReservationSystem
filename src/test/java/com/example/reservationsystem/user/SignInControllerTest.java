package com.example.reservationsystem.user;

import com.example.reservationsystem.common.ControllerTest;
import com.example.reservationsystem.user.signin.application.SignInService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class SignInControllerTest extends ControllerTest {

    @MockBean
    private SignInService signInService;

    @Test
    void 유저는_로그인을_할_수_있다() {
        // given
        SignInRequest signInRequest = new SignInRequest("hanhan@naver.com", "12345");
        given(signInService.signIn(any(String.class), any(String.class))).willReturn("accessToken");

        // when
        mockMvc.perform(post("/sign-in")
                .content(signInRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect()


        // then
    }

}
