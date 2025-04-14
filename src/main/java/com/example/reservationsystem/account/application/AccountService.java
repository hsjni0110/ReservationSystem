package com.example.reservationsystem.account.application;

import com.example.reservationsystem.account.domain.model.Account;
import com.example.reservationsystem.account.infra.repository.AccountRepository;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.account.application.dto.BalanceResponse;
import com.example.reservationsystem.account.exception.AccountException;
import com.example.reservationsystem.user.signup.domain.model.User;
import com.example.reservationsystem.user.signup.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.reservationsystem.account.exception.AccountExceptionType.ACCOUNT_NOT_FOUND;
import static com.example.reservationsystem.account.exception.AccountExceptionType.INVALID_RECHARGED_PRICE;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final BalanceLockManager balanceLockManager;
    private final AccountRepository accountRepository;

    public BalanceResponse recharge( long userId, long amount ) {
        validate( amount );
        try {
            Money recharged = balanceLockManager.rechargeWithLock( userId, amount );
            return new BalanceResponse( recharged.getAmount().longValue() );
        } catch ( Exception e ) {
            throw new AccountException( INVALID_RECHARGED_PRICE );
        }
    }

    private void validate( long amount ) {
        if ( amount < 0 ) {
            throw new AccountException( INVALID_RECHARGED_PRICE );
        }
    }

    public Long debit( long userId, long amount ) {
        validate( amount );
        return balanceLockManager.depositWithLock(userId, amount);
    }

    @Transactional( readOnly = true )
    public BalanceResponse getBalance( long userId ) {
        Account account = accountRepository.findByUserUserId( userId )
                .orElseThrow(() -> new AccountException( ACCOUNT_NOT_FOUND ));
        return new BalanceResponse( account.getAmount().getAmount().longValue() );
    }

}
