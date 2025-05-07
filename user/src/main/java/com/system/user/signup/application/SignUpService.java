package com.system.user.signup.application;

import com.system.domain.event.UserSignUpEvent;
import com.system.user.signup.domain.model.User;
import com.system.user.signup.infra.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long signUp(String email, String password, String name, String phoneNumber) {
        User user = User.create(email, password, name, phoneNumber);
        User saved = userRepository.save( user );
        applicationEventPublisher.publishEvent( new UserSignUpEvent( saved.getUserId() ) );
        return saved.getUserId();
    }

}
