package com.example.reservationsystem.account.application;

import com.example.reservationsystem.account.domain.model.Account;
import com.example.reservationsystem.account.infra.repository.AccountRepository;
import com.example.reservationsystem.account.exception.AccountException;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.user.signup.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.reservationsystem.account.exception.AccountExceptionType.ACCOUNT_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class BalanceManager {

    private final AccountRepository accountRepository;

    @Transactional
    public Money recharge(User user, long amount ) {
        Account account = accountRepository.findByUserForUpdate( user ).orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        return account.recharge( Money.wons(amount) );
    }

    @Transactional
    public Long deposit( User user, long amount ) {
        Account account = accountRepository.findByUserForUpdate( user ).orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        account.deposit( Money.wons( amount ) );
        return account.getAccountId();
    }

}
