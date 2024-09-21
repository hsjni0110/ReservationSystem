package com.example.reservationsystem.user.signin.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final String accessExpirationTime;
    private final String refreshExpirationTime;


    public JwtProvider(
            final String secretKey,
            @Value("${security.jwt.access-expiration-time}") final Long accessExpirationTime,
            @Value("${security.jwt.refresh-expiration-time}") final Long refreshExpirationTime
    ) {

    }

}
