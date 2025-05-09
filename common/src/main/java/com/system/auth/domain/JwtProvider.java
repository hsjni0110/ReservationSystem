package com.system.auth.domain;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;


    public JwtProvider(
            @Value("${security.jwt.secret-key}") final String secretKey,
            @Value("${security.jwt.access-expiration-time}") final Long accessExpirationTime,
            @Value("${security.jwt.refresh-expiration-time}") final Long refreshExpirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public String generateToken(final String subject) {
        return createAccessToken(subject, accessExpirationTime);
    }

    private String createAccessToken(String subject, Long accessExpirationTime) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }

}
