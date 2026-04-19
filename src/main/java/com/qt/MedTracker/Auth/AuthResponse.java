package com.qt.MedTracker.Auth;

import com.qt.MedTracker.User.UserResponse;

public record AuthResponse(String token, UserResponse user) {
}
