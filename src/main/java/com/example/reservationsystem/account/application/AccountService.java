package com.example.reservationsystem.account.application;

import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.account.application.dto.BalanceResponse;
import com.example.reservationsystem.account.exception.AccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.reservationsystem.account.exception.AccountExceptionType.INVALID_RECHARGED_PRICE;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final BalanceLockManager balanceLockManager;

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

}
