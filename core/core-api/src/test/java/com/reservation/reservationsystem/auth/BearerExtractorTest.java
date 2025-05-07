package com.reservation.reservationsystem.auth;

import com.reservation.reservationsystem.auth.domain.BearerExtractor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BearerExtractorTest {

    @Test
    void Bearer뒤의_accessToken을_제대로_추출하여야_한다() {
        // given
        BearerExtractor bearerExtractor = new BearerExtractor();
        String givenHeader = "Bearer accessTokenblabla";

        // when
        String token = bearerExtractor.extract(givenHeader);

        // then
        assertEquals(token, "accessTokenblabla");
    }

}
