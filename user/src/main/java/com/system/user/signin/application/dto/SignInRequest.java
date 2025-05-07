package com.system.user.signin.application.dto;

public record SignInRequest(
        String email,
        String password
) {
}
