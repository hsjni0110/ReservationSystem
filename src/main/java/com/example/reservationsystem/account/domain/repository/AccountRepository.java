package com.example.reservationsystem.account.domain.repository;

import com.example.reservationsystem.account.domain.Account;
import com.example.reservationsystem.user.signup.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUser(User user);

}
