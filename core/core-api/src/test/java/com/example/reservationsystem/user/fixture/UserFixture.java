package com.example.reservationsystem.user.fixture;

import com.example.reservationsystem.user.signup.domain.model.User;

import static com.example.reservationsystem.common.FixtureCommon.fixtureMonkey;

public class UserFixture {

    public static User 유저() {
        return fixtureMonkey.giveMeBuilder(User.class)
                .sample();
    }

    public static User 유저(Long id) {
        return fixtureMonkey.giveMeBuilder(User.class)
                .set("userId", id)
                .sample();
    }

}
