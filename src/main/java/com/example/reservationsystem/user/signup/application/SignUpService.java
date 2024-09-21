package com.example.reservationsystem.user.signup.application;

import com.example.reservationsystem.user.signup.domain.User;
import com.example.reservationsystem.user.signup.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;

    @Transactional
    public Long signUp(String email, String password, String name, String phoneNumber) {
        User user = User.create(email, password, name, phoneNumber);
        User saved = userRepository.save(user);
        return saved.getUserId();
    }

}
