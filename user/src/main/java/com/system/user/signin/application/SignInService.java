package com.system.user.signin.application;

import com.system.auth.domain.JwtProvider;
import com.system.user.signin.application.dto.SignInResponse;
import com.system.user.signin.exception.UserAuthException;
import com.system.user.signup.domain.model.User;
import com.system.user.signup.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.system.user.signin.exception.UserAuthExceptionType.EMAIL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public SignInResponse signIn(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserAuthException(EMAIL_NOT_FOUND));
        user.validatePassword(password);
        String accessToken = jwtProvider.generateToken(user.getUserId().toString());
        return new SignInResponse(accessToken);
    }

}
