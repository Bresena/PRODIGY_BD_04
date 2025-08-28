package com.user.model;

import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Positive(message = "Age must be positive")
    private int age;
}
