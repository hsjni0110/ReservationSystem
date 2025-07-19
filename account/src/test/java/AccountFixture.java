import com.reservationsystem.FixtureCommon;
import com.system.domain.Money;
import com.system.domain.model.Account;

public class AccountFixture {

    public static Account 만원_통장() {
        return FixtureCommon.fixtureMonkey.giveMeBuilder(Account.class)
                .setNull("accountId")
                .set("amount", Money.wons(10000))
                .sample();
    }

}
