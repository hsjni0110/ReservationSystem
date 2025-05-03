package com.example.reservationsystem.user.signup.infra.repository;

import com.example.reservationsystem.user.signup.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);

     default User getByIdOrThrow(Long id) {
          return findById(id).orElseThrow(() -> new RuntimeException("User not found"));
     }

}
