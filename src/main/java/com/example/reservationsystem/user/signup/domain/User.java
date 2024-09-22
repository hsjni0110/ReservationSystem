package com.example.reservationsystem.user.signup.domain;

import com.example.reservationsystem.common.domain.BaseEntity;
import com.example.reservationsystem.user.signin.exception.UserAuthException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.reservationsystem.user.signin.exception.UserAuthExceptionType.INVALID_PASSWORD;

@Table(name = "USERS")
@Entity
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long userId;

    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    private User(String email, String password, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public static User create(String email, String password, String name, String phoneNumber) {
        return new User(email, password, name, phoneNumber);
    }


    public void validatePassword(String givenPassword) {
        if (!password.equals(givenPassword)) {
            throw new UserAuthException(INVALID_PASSWORD);
        }
    }

}
