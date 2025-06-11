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
    private final BalanceManager balanceManager;

    public void recharge(long userId, long amount) {
        validateInvalidUnit(amount);
        try {
            Money recharged = balanceLockManager.rechargeWithLock(userId, amount);
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new RuntimeException( e );
        }
    }

    public BalanceResponse getBalance(Long userId) {
        return balanceManager.getBalance(userId);
    }

    public Long debit(long userId, long amount) {
        validateInvalidUnit(amount);
        return balanceLockManager.depositWithLock(userId, amount);
    }

    private void validateInvalidUnit(long amount) {
        if (amount < 0) {
            throw new AccountException(INVALID_RECHARGED_PRICE);
        }
    }

    @Transactional
    public void createAccount(Long userId) {
        Account account = new Account(userId);
        accountRepository.save(account);
    }

}
