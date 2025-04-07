package com.example.reservationsystem.account.application;

import com.example.reservationsystem.common.annotation.DistributedSimpleLock;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.user.signup.domain.model.User;
import com.example.reservationsystem.user.signup.infra.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class BalanceLockManager {

    private final UserRepository userRepository;
    private final BalanceManager balanceManager;

    public BalanceLockManager( UserRepository userRepository, BalanceManager balanceManager ) {
        this.userRepository = userRepository;
        this.balanceManager = balanceManager;
    }

    @DistributedSimpleLock(
            key = "user:#userId",
            waitTime = 5,
            releaseTime = 10
    )
    public Money rechargeWithLock( Long userId, Long amount ) {
        User user = userRepository.getByIdOrThrow( userId );
        return balanceManager.recharge( user, amount );
    }

    @DistributedSimpleLock(
            key = "user:#userId",
            waitTime = 5,
            releaseTime = 10
    )
    public void depositWithLock( Long userId, Long amount ) {
        User user = userRepository.getByIdOrThrow( userId );
        balanceManager.deposit( user, amount );
    }

}
