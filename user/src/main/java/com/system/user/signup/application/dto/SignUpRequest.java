package com.system.user.signup.application.dto;

public record SignUpRequest(
        String email,
        String password,
        String name,
        String phoneNumber
) {
}
