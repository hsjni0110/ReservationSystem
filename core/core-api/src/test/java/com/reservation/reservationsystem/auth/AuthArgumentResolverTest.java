package com.reservation.reservationsystem.auth;

import com.reservation.reservationsystem.auth.domain.Auth;
import com.reservation.reservationsystem.auth.domain.AuthArgumentResolver;
import com.reservation.reservationsystem.common.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@WebMvcTest(AuthArgumentResolver.class)
@DisplayName("사용자 인가 인터셉터(AuthArgumentResolver)는")
public class AuthArgumentResolverTest extends ControllerTest {

    @Autowired
    private HandlerMethodArgumentResolver authArgumentResolver;

    @BeforeEach
    public void setUp() {
        authArgumentResolver = new AuthArgumentResolver(bearerExtractor, jwtProvider);
    }

    @Test
    public void 유효한토큰일때_사용자아이디를반환한다() throws Exception {
        // given
        String token = "Bearer valid.token";
        String userId = "12345";

        when(bearerExtractor.extract(any())).thenReturn("valid.token");
        when(jwtProvider.getSubject("valid.token")).thenReturn(userId);

        // when
        Long result = (Long) authArgumentResolver.resolveArgument(
                /* MethodParameter */ mock(MethodParameter.class),
                /* ModelAndViewContainer */ null,
                /* NativeWebRequest */ mock(NativeWebRequest.class),
                /* WebDataBinderFactory */ null
        );

        // then
        assertEquals(Long.valueOf(userId), result);
    }

    @Test
    public void Auth애너테이션이있을때_지원한다() throws Exception {
        // given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(Auth.class)).thenReturn(true);

        // when
        boolean supports = authArgumentResolver.supportsParameter(parameter);

        // then
        assertTrue(supports);
    }

    @Test
    public void Auth애너테이션이없을때_지원하지않는다() throws Exception {
        // given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(Auth.class)).thenReturn(false);

        // when
        boolean supports = authArgumentResolver.supportsParameter(parameter);

        // then
        assertFalse(supports);
    }

}

