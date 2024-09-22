package com.example.reservationsystem.common;

import com.example.reservationsystem.auth.BearerExtractor;
import com.example.reservationsystem.user.signin.domain.JwtProvider;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ControllerTest {

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected BearerExtractor bearerExtractor;

}
