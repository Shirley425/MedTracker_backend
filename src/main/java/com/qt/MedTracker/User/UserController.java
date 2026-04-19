package com.qt.MedTracker.User;

import com.qt.MedTracker.SlackAPI.SlackWebhookRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{userId}/slack-webhook")
    public User updateSlackWebhook(@PathVariable Long userId, @RequestBody SlackWebhookRequest request) {
        return userService.updateSlackWebhook(userId, request.getWebhookUrl());
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(
            @PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

    @Transactional
    public void updateUser(Long userId, String name, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id" + userId + "does not exist"
                ));
        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }

        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(user.getEmail(), email)) {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isPresent()) {
                throw new IllegalArgumentException("email already exists");
            }
            user.setEmail(email);
        }
    }
}
