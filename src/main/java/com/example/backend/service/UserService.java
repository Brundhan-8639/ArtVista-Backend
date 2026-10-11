
package com.example.backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser(User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return null;
        }

        String normalizedEmail = user.getEmail().trim();
        if (normalizedEmail.isEmpty()) {
            return null;
        }

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            return null;
        }

        user.setEmail(normalizedEmail);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        if (email == null || password == null) {
            return null;
        }

        String normalizedEmail = email.trim();
        if (normalizedEmail.isEmpty()) {
            return null;
        }

        User user = userRepository.findByEmail(normalizedEmail).orElse(null);

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }

}
