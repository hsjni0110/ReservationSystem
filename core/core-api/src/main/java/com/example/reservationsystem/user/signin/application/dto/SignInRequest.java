package com.example.reservationsystem.user.signin.application.dto;

public record SignInRequest(
        String email,
        String password
) {
}
