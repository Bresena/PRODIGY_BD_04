package com.user.service;

import java.util.List;

import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import com.user.model.User;
import com.user.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Cache results for "getAllUsers"
    @Cacheable(value = "usersCache")
    public List<User> getAllUsers() {
        System.out.println("Fetching from DB..."); // to test caching
        return userRepository.findAll();
    }

    // ✅ Cache eviction when a user is updated
    @CacheEvict(value = "usersCache", allEntries = true)
    public User updateUser(String id, User user) {
        User existing = userRepository.findById(id).orElseThrow();
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        return userRepository.save(existing);
    }

    // ✅ Cache eviction when a user is deleted
    @CacheEvict(value = "usersCache", allEntries = true)
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // ✅ Cache eviction when new user is added
    @CacheEvict(value = "usersCache", allEntries = true)
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
