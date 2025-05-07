package com.reservation.reservationsystem.common;

import com.reservation.reservationsystem.auth.domain.BearerExtractor;
import com.reservation.reservationsystem.user.signin.domain.JwtProvider;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ControllerTest {

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected BearerExtractor bearerExtractor;

    protected void 사용자_인가() {
        when(bearerExtractor.extract(any())).thenReturn("valid.token");
        when(jwtProvider.getSubject("valid.token")).thenReturn("1");
    }

}
