package com.example.reservationsystem.user.signin.dto;

public record SignInRequest(
        String email,
        String password
) {
}
