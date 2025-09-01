package com.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    private String name;

    @Email
    @Column(unique = true, nullable=false)
    private String email;

    private String password; // hashed password

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, USER, OWNER
    
    @Positive
    private int age;
}
