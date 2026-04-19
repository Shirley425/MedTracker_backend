package com.qt.MedTracker.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("user with id " + userId + " does not exist"));
    }

    public User saveUser(User user) {
        Optional<User> userOptional = userRepository
                .findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalStateException("password required");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (rawPassword == null || rawPassword.isBlank()
                || user.getPassword() == null
                || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    public User updateSlackWebhook(Long userId, String webhookUrl) {
        User user = getUserById(userId);
        user.setSlackWebhookUrl(webhookUrl == null ? null : webhookUrl.trim());
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists) {
            throw new IllegalStateException(
                    "user with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }
}
