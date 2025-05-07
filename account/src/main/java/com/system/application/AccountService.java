package com.system.application;

import com.system.domain.model.Account;
import com.system.domain.Money;
import com.system.infra.repository.AccountRepository;
import com.system.application.dto.BalanceResponse;
import com.system.exception.AccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.system.exception.AccountExceptionType.ACCOUNT_NOT_FOUND;
import static com.system.exception.AccountExceptionType.INVALID_RECHARGED_PRICE;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final BalanceLockManager balanceLockManager;
    private final AccountRepository accountRepository;

    public BalanceResponse recharge( long userId, long amount ) {
        validate( amount );
        try {
            Money recharged = balanceLockManager.rechargeWithLock( userId, amount );
            return new BalanceResponse( recharged.getAmount().longValue() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new RuntimeException( e );
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
        Account account = accountRepository.findByUserId( userId )
                .orElseThrow(() -> new AccountException( ACCOUNT_NOT_FOUND ));
        return new BalanceResponse( account.getAmount().getAmount().longValue() );
    }

    @Transactional
    public void createAccount( Long userId ) {
        Account account = new Account(userId);
        accountRepository.save( account );
    }

}
