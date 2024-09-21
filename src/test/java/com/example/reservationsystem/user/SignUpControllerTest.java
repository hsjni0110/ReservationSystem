package com.example.reservationsystem.user;

import com.example.reservationsystem.common.ControllerTest;
import com.example.reservationsystem.user.signup.application.SignUpService;
import com.example.reservationsystem.user.signup.dto.SignUpRequest;
import com.example.reservationsystem.user.signup.presentation.SignUpController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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

@WebMvcTest(SignUpController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("회원가입 컨트롤러(SignUpController)는 ")
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
