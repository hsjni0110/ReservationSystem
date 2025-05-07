package com.reservation.reservationsystem.user.service;

import com.reservation.reservationsystem.common.ServiceTest;
import com.system.infra.repository.AccountRepository;
import com.system.user.signup.application.SignUpService;
import com.system.user.signup.domain.model.User;
import com.system.user.signup.infra.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("회원가입 서비스(SignUpService)는")
public class SignUpServiceTest extends ServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    void 사용자를_단_한번만_생성할_수_있다() {
        // given
        given(userRepository.save(any())).willReturn(new User());

        // when
        signUpService.signUp("hanhan@naver.com", "12345", "hanhan", "010-0000-0000");

        // then
        then(userRepository).should(times(1)).save(any(User.class));
    }

}
