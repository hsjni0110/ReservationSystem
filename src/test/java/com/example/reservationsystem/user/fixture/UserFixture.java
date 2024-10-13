package com.example.reservationsystem.user.fixture;

import com.example.reservationsystem.user.signup.domain.User;

import static com.example.reservationsystem.common.FixtureCommon.fixtureMonkey;

public class UserFixture {

    public static User 유저() {
        return fixtureMonkey.giveMeBuilder(User.class)
                .sample();
    }

}
