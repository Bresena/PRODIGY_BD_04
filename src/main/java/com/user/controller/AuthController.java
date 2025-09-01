package com.user.controller;

import com.user.model.User;
import com.user.model.Role;
import com.user.repository.UserRepository;
import com.user.security.JwtUtil;
import com.user.dto.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        // hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER); // force default USER
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // ✅ Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    @PostMapping("/admin/setRole")
    public ResponseEntity<?> setRole(@RequestParam String email, @RequestParam String role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
        return ResponseEntity.ok("Role updated successfully");
    }
}