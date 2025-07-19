package com.system.user.signup.application;

import com.system.user.signup.application.dto.UserProfileResponse;
import com.system.user.signup.domain.model.User;
import com.system.user.signup.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.getByIdOrThrow(userId);
        return new UserProfileResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber()
        );
    }
}