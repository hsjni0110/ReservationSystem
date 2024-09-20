package com.example.reservationsystem.user.signup.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Table(name = "USERS")
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
