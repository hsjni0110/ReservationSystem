package com.example.reservationsystem.user;

import com.example.reservationsystem.common.ServiceTest;
import com.example.reservationsystem.user.signup.application.SignUpService;
import com.example.reservationsystem.user.signup.domain.User;
import com.example.reservationsystem.user.signup.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("회원가입 서비스(SignUpService)는")
public class UserServiceTest extends ServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    void 사용자를_단_한번만_생성할_수_있다() {
        // given, when
        signUpService.signUp("hanhan@naver.com", "12345", "hanhan", "010-0000-0000");

        // then
        then(userRepository).should(times(1)).save(any(User.class));
    }

}
