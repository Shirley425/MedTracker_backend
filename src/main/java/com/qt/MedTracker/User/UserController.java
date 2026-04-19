package com.qt.MedTracker.User;

import com.qt.MedTracker.SlackAPI.SlackConnectionRequest;
import com.qt.MedTracker.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        SecurityUtils.requireAdmin();
        return userService.getAllUsers().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        return UserResponse.from(userService.getUserById(SecurityUtils.getCurrentUser().getUserId()));
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        SecurityUtils.requireAdmin();
        return UserResponse.from(userService.createUser(request));
    }

    @PutMapping("/{userId}/slack-connection")
    public UserResponse updateSlackConnection(@PathVariable Long userId, @Valid @RequestBody SlackConnectionRequest request) {
        SecurityUtils.requireCurrentUserOrAdmin(userId);
        return UserResponse.from(userService.updateSlackMemberId(userId, request.getSlackMemberId()));
    }

    @PutMapping("/me")
    public UserResponse updateMyProfile(@Valid @RequestBody UserUpdateRequest request) {
        return UserResponse.from(userService.updateUserProfile(SecurityUtils.getCurrentUser().getUserId(), request));
    }

    @PutMapping("/me/slack-connection")
    public UserResponse updateMySlackConnection(@Valid @RequestBody SlackConnectionRequest request) {
        return UserResponse.from(
                userService.updateSlackMemberId(SecurityUtils.getCurrentUser().getUserId(), request.getSlackMemberId())
        );
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(
            @PathVariable("userId") Long userId) {
        SecurityUtils.requireCurrentUserOrAdmin(userId);
        userService.deleteUser(userId);
    }
}
