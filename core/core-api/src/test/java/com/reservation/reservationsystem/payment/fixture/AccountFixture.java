package com.reservation.reservationsystem.payment.fixture;

import com.system.domain.Money;
import com.system.domain.model.Account;

import static com.reservation.reservationsystem.common.FixtureCommon.fixtureMonkey;

public class AccountFixture {

    public static Account 만원_통장() {
        return fixtureMonkey.giveMeBuilder(Account.class)
                .setNull("accountId")
                .set("amount", Money.wons(10000))
                .sample();
    }

}
