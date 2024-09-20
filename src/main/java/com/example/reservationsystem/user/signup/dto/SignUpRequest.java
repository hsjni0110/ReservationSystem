package com.example.reservationsystem.user.signup.dto;

public record SignUpRequest(
        String email,
        String password,
        String name,
        String phoneNumber
) {
}
