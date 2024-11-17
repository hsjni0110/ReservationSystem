package com.example.reservationsystem.payment.fixture;

import com.example.reservationsystem.account.domain.Account;
import com.example.reservationsystem.account.domain.Money;

import static com.example.reservationsystem.common.FixtureCommon.fixtureMonkey;

public class AccountFixture {

    public static Account 만원_통장() {
        return fixtureMonkey.giveMeBuilder(Account.class)
                .setNull("accountId")
                .set("amount", Money.wons(10000))
                .sample();
    }

}
