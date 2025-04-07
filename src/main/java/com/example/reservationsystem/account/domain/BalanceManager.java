package com.example.reservationsystem.account.domain;

import com.example.reservationsystem.account.domain.repository.AccountRepository;
import com.example.reservationsystem.account.exception.AccountException;
import com.example.reservationsystem.common.domain.Money;
import com.example.reservationsystem.user.signup.domain.User;
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
    public void deposit( User user, long amount ) {
        Account account = accountRepository.findByUserForUpdate( user ).orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        account.deposit( Money.wons( amount ) );
    }

}
