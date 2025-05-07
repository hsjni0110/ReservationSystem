package com.system.application;

import com.system.annotation.DistributedSimpleLock;
import com.system.domain.Money;
import com.system.user.signup.domain.model.User;
import org.springframework.stereotype.Component;
import com.system.user.signup.infra.repository.UserRepository;

@Component
public class BalanceLockManager {

    private final UserRepository userRepository;
    private final BalanceManager balanceManager;

    public BalanceLockManager( UserRepository userRepository, BalanceManager balanceManager ) {
        this.userRepository = userRepository;
        this.balanceManager = balanceManager;
    }

    @DistributedSimpleLock(
            key = "'user:' + #userId",
            waitTime = 5,
            releaseTime = 10
    )
    public Money rechargeWithLock(Long userId, Long amount ) {
        User user = userRepository.getByIdOrThrow( userId );
        return balanceManager.recharge( user, amount );
    }

    @DistributedSimpleLock(
            key = "user:#userId",
            waitTime = 5,
            releaseTime = 10
    )
    public Long depositWithLock( Long userId, Long amount ) {
        User user = userRepository.getByIdOrThrow( userId );
        return balanceManager.deposit( user, amount );
    }

}
