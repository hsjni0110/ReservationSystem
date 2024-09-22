package com.example.reservationsystem.user.signin.application;

import com.example.reservationsystem.user.signin.domain.JwtProvider;
import com.example.reservationsystem.user.signin.dto.SignInResponse;
import com.example.reservationsystem.user.signin.exception.UserAuthException;
import com.example.reservationsystem.user.signup.domain.User;
import com.example.reservationsystem.user.signup.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.reservationsystem.user.signin.exception.UserAuthExceptionType.EMAIL_NOT_FOUND;

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
