package com.reservation.reservationsystem.user.domain;

import com.reservation.reservationsystem.common.DomainTest;
import com.reservation.reservationsystem.user.signin.domain.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("토큰 생성기(JwtProvider)는")
public class JwtProviderTest extends DomainTest {

    @Test
    void 토큰을_생성할_수_있다() {
        // given
        JwtProvider jwtProvider = new JwtProvider("7J206rKD7J2AIOyLnO2BrOumv+2CpOyeheuLiOuLpC4g7YWM7Iqk7Yq47Jqp", 12000000L, 20000000L);

        // when
        String accessToken = jwtProvider.generateToken("1");

        // then
        assertNotNull(accessToken);
    }

    @Test
    void 인자로_받은_토큰을_파싱할_수_있다() {
        // given
        JwtProvider jwtProvider = new JwtProvider("7J206rKD7J2AIOyLnO2BrOumv+2CpOyeheuLiOuLpC4g7YWM7Iqk7Yq47Jqp", 12000000L, 20000000L);

        String accessToken = jwtProvider.generateToken("1");

        // when
        String subject = jwtProvider.getSubject(accessToken);

        // then
        assertEquals(Long.valueOf(subject), 1L);
    }

}
