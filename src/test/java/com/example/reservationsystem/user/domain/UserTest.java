package com.example.reservationsystem.user.domain;

import com.example.reservationsystem.common.DomainTest;
import com.example.reservationsystem.user.signin.exception.UserAuthException;
import com.example.reservationsystem.user.signup.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("유저(User)는")
public class UserTest extends DomainTest {

    @Test
    void 회원을_생성할_수_있다() {
        User user = User.create("hanhan@naver.com", "12345", "hanhan", "010-0000-0000");
    }

    @Test
    void 비밀번호를_검증할_수_있다() {
        // given
        User user = User.create("hanhan@naver.com", "12345", "hanhan", "010-0000-0000");

        // when, then
        user.validatePassword("12345");
    }

    @Test
    void 비밀번호가_유효하지_않을때_예외를_던진다() {
        // given
        User user = User.create("hanhan@naver.com", "12345", "hanhan", "010-0000-0000");

        // when
        String invalidPassword = "123";

        // then
        assertThrows(UserAuthException.class, () -> {
            user.validatePassword(invalidPassword);
        });
    }

}
