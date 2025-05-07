package com.system.infra.repository;

import com.system.domain.model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select a from Account a where a.userId = :userId")
    Optional<Account> findByUserForUpdate( Long userId );

    Optional<Account> findByUserId( Long userId );

}

