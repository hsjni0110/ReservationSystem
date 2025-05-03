package com.example.reservationsystem.user.signup.application.dto;

public record SignUpRequest(
        String email,
        String password,
        String name,
        String phoneNumber
) {
}
