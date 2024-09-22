package com.example.reservationsystem.user;

import com.example.reservationsystem.common.ServiceTest;
import com.example.reservationsystem.user.signin.application.SignInService;
import com.example.reservationsystem.user.signin.domain.JwtProvider;
import com.example.reservationsystem.user.signin.dto.SignInResponse;
import com.example.reservationsystem.user.signup.domain.User;
import com.example.reservationsystem.user.signup.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@DisplayName("로그인 서비스(SignInService)는 ")
public class SignInServiceTest extends ServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private SignInService signInService;

    @Test
    void 로그인하면_AccessToken을_반환_받는다() {
        // given
        given(userRepository.findByEmail("hanhan@naver.com")).willReturn(Optional.of(user));
        given(user.getUserId()).willReturn(1L);
        given(jwtProvider.generateToken("1")).willReturn("asdksfjsadfkj");

        // when
        SignInResponse signInResponse = signInService.signIn("hanhan@naver.com", "12345");

        // then
        assertNotNull(signInResponse.accessToken());
    }

}
