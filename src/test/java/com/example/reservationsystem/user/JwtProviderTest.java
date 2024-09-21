package com.example.reservationsystem.user;

import com.example.reservationsystem.common.DomainTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtProviderTest extends DomainTest {

    @Test
    void 토큰을_생성할_수_있다() {
        // given
        JwtProvider jwtProvider = new JwtProvider();

        // when
        String accessToken = jwtProvider.generateToken(1L);

        // then
        assertNotNull(accessToken);
    }

    @Test
    void 인자로_받은_토큰을_파싱할_수_있다() {
        // given
        JwtProvider jwtProvider = new JwtProvider();
        String accessToken = jwtProvider.generateToken(1L);

        // when
        String subject = jwtProvider.getSubject(accessToken);

        // then
        assertEquals(Long.valueOf(subject), 1L);
    }

}
