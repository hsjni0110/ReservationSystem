package com.reservationsystem;

import com.system.user.signup.domain.model.User;

public class UserFixture {

    public static User 유저() {
        return FixtureCommon.fixtureMonkey.giveMeBuilder(User.class)
                .sample();
    }

    public static User 유저(Long id) {
        return FixtureCommon.fixtureMonkey.giveMeBuilder(User.class)
                .set("userId", id)
                .sample();
    }

}
