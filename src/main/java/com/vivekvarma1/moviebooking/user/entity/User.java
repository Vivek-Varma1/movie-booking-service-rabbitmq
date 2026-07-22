package com.vivekvarma1.moviebooking.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    public User(String name, String emailAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
    }

//    public String getUserName() {
//        return name;
//    }
//
//    public String getUserEmail() {
//        return emailAddress;
//    }
}