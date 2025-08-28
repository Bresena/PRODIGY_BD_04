package com.user.controller;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.user.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final Map<UUID, User> userStore = new HashMap<>();

    // CREATE
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is required");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");
        }
        if (user.getAge() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Age must be positive");
        }

        UUID id = UUID.randomUUID();
        user.setId(id);
        userStore.put(id, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(new ArrayList<>(userStore.values()));
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        User user = userStore.get(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User updatedUser) {
        User existingUser = userStore.get(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (updatedUser.getName() != null && !updatedUser.getName().isBlank()) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null &&
                updatedUser.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getAge() > 0) {
            existingUser.setAge(updatedUser.getAge());
        }

        userStore.put(id, existingUser);
        return ResponseEntity.ok(existingUser);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        User removedUser = userStore.remove(id);
        if (removedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.noContent().build();
    }
}
