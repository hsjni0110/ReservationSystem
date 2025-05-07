package com.system.application;

import com.system.domain.model.Account;
import com.system.domain.Money;
import com.system.infra.repository.AccountRepository;
import com.system.exception.AccountException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.system.user.signup.domain.model.User;

import static com.system.exception.AccountExceptionType.ACCOUNT_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class BalanceManager {

    private final AccountRepository accountRepository;

    @Transactional
    public Money recharge( User user, long amount ) {
        Account account = accountRepository.findByUserForUpdate( user.getUserId() ).orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        return account.recharge( Money.wons(amount) );
    }

    @Transactional
    public Long deposit( User user, long amount ) {
        Account account = accountRepository.findByUserForUpdate( user.getUserId() ).orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        account.deposit( Money.wons( amount ) );
        return account.getAccountId();
    }

}
