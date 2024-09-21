package com.example.reservationsystem.user;

import com.example.reservationsystem.common.DomainTest;
import com.example.reservationsystem.user.signup.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("유저(User)는")
public class UserTest extends DomainTest {

    @Test
    void 회원을_생성할_수_있다() {
        User user = User.create("hanhan@naver.com", "12345", "hanhan", "010-0000-0000");
    }
}
