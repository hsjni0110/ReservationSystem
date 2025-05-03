package com.example.reservationsystem.account.infra.repository;

import com.example.reservationsystem.account.domain.model.Account;
import com.example.reservationsystem.user.signup.domain.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select a from Account a where a.user = :user")
    Optional<Account> findByUserForUpdate( User user );

    Optional<Account> findByUserUserId( Long userId );

}

