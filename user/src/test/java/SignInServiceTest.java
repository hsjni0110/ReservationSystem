import com.system.auth.domain.JwtProvider;
import com.system.user.signin.application.SignInService;
import com.system.user.signin.application.dto.SignInResponse;
import com.system.user.signup.domain.model.User;
import com.system.user.signup.infra.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@DisplayName("로그인 서비스(SignInService)는 ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SignInServiceTest {

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
