package com.user.model;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;

    @NotBlank
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @Positive
    private int age;

    public User() {
        this.id = UUID.randomUUID().toString();
    }
}
