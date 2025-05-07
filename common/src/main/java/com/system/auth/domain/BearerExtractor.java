package com.system.auth.domain;

import com.system.auth.exception.AuthException;
import org.springframework.stereotype.Component;

import static com.system.auth.exception.AuthExceptionType.INVALID_TOKEN_FORMAT;

@Component
public class BearerExtractor {

    private static final String BEARER = "Bearer";

    public String extract(String header) {
        if (header != null && header.startsWith(BEARER)) {
            return header.substring(BEARER.length()).trim();
        }
        throw new AuthException(INVALID_TOKEN_FORMAT);
    }

}
