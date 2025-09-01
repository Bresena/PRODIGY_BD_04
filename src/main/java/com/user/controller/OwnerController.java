package com.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.user.model.User;
import com.user.repository.UserRepository;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {
	
	@Autowired
    private UserRepository userRepository;
	
	@GetMapping("/stats")
    @PreAuthorize("hasAuthority('OWNER')")
    public String getSystemStats() {
        return "Confidential system stats - visible only to OWNER";
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasAuthority('OWNER')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
