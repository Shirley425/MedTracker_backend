package com.qt.MedTracker.User;

import com.qt.MedTracker.security.AppUserPrincipal;
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

    public User getUserBySlackMemberId(String slackMemberId) {
        return userRepository.findBySlackMemberId(slackMemberId)
                .orElseThrow(() -> new IllegalStateException("user with Slack member id " + slackMemberId + " does not exist"));
    }

    public User saveUser(User user) {
        String normalizedEmail = user.getEmail() == null ? null : user.getEmail().trim().toLowerCase();
        user.setEmail(normalizedEmail);

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

    public User createUser(UserCreateRequest request) {
        User user = new User(
                request.name().trim(),
                request.birthDate(),
                request.email().trim().toLowerCase(),
                request.password(),
                request.role().trim().toUpperCase()
        );
        return saveUser(user);
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

    public AppUserPrincipal toPrincipal(User user) {
        return new AppUserPrincipal(user.getId(), user.getEmail(), user.getRole());
    }

    public User updateSlackMemberId(Long userId, String slackMemberId) {
        User user = getUserById(userId);
        String normalizedSlackMemberId = slackMemberId == null ? null : slackMemberId.trim();

        if (normalizedSlackMemberId != null && !normalizedSlackMemberId.isBlank()) {
            userRepository.findBySlackMemberId(normalizedSlackMemberId)
                    .filter(existingUser -> !existingUser.getId().equals(userId))
                    .ifPresent(existingUser -> {
                        throw new IllegalStateException("Slack member ID is already linked to another account");
                    });
        }

        user.setSlackMemberId(normalizedSlackMemberId);
        return userRepository.save(user);
    }

    public User updateUserProfile(Long userId, UserUpdateRequest request) {
        User user = getUserById(userId);
        String normalizedEmail = request.email().trim().toLowerCase();

        userRepository.findUserByEmail(normalizedEmail)
                .filter(existingUser -> !existingUser.getId().equals(userId))
                .ifPresent(existingUser -> {
                    throw new IllegalStateException("email already exists");
                });

        user.setName(request.name().trim());
        user.setBirthDate(request.birthDate());
        user.setEmail(normalizedEmail);
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
