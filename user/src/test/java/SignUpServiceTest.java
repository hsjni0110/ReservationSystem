import com.system.domain.event.UserSignUpEvent;
import com.system.user.signup.application.SignUpService;
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
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@DisplayName("회원가입 서비스(SignUpService)는")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SignUpServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    void 사용자를_단_한번만_생성할_수_있다() {
        // given
        given(userRepository.save(any())).willReturn(new User());
        willDoNothing().given(publisher).publishEvent(any(UserSignUpEvent.class));

        // when
        signUpService.signUp("hanhan@naver.com", "12345", "hanhan", "010-0000-0000");

        // then
        then(userRepository).should(times(1)).save(any(User.class));
    }

}
