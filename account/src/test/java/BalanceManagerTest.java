import com.reservationsystem.UserFixture;
import com.system.application.BalanceManager;
import com.system.domain.Money;
import com.system.domain.model.Account;
import com.system.infra.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.system.user.signup.domain.model.User;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("잔액 충전 매니저(BalanceManager)는")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class BalanceManagerTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BalanceManager balanceManager;

    @Test
    void 잔액을_충전하면_총_계좌에서_잔액이_더해진다() {
        // given
        Long amount = 10000L;
        User 유저 = UserFixture.유저();
        Account 만원_통장 = AccountFixture.만원_통장();
        given(accountRepository.findByUserForUpdate(any(Long.class))).willReturn(Optional.of(만원_통장));

        // when
        Money totalAmount = balanceManager.recharge(유저, amount);

        // then
        assertThat(totalAmount.getMoney()).isEqualTo(BigDecimal.valueOf(20000));
    }

}
