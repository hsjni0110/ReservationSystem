package com.reservationsystem.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservationsystem.support.ControllerTest;
import com.system.reservationsystem.controller.SignUpController;
import com.system.user.signup.application.SignUpService;
import com.system.user.signup.application.dto.SignUpRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원가입 컨트롤러(SignUpController)는 ")
@WebMvcTest(SignUpController.class)
public class SignUpControllerTest extends ControllerTest {

    @MockBean
    private SignUpService signupService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 유저_회원가입을_할_수_있다() throws Exception{
        // given
        SignUpRequest request = new SignUpRequest("hanhan@naver.com", "12345", "hans", "010-0000-0000");

        // when
        given(signupService.signUp(any(String.class), any(String.class), any(String.class), any(String.class))).willReturn(1L);

        // then
        mockMvc.perform(post("/sign-up")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}
