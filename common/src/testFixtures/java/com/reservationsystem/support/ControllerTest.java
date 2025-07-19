package com.reservationsystem.support;

import com.system.auth.domain.BearerExtractor;
import com.system.auth.domain.JwtProvider;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ControllerTest {

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected BearerExtractor bearerExtractor;

    protected void 사용자_인가() {
        Mockito.when(bearerExtractor.extract(ArgumentMatchers.any())).thenReturn("valid.token");
        Mockito.when(jwtProvider.getSubject("valid.token")).thenReturn("1");
    }

}
