package com.system.user.signup.application.dto;

public record UserProfileResponse(
        Long userId,
        String email,
        String name,
        String phoneNumber
) {
}