package com.example.reservationsystem.user.signup.application;

import com.example.reservationsystem.user.signup.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Transactional
    public void signUp(String email, String password, String name, String phoneNumber) {
        User user = User.create(email, password, name, phoneNumber);
    }

}
