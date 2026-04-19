package com.qt.MedTracker.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static AppUserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserPrincipal principal)) {
            throw new IllegalStateException("Authenticated user not found");
        }
        return principal;
    }

    public static boolean isAdmin() {
        return getCurrentUser().getAuthorities().stream()
                .anyMatch(authority ->
                        "ROLE_ADMIN".equals(authority.getAuthority())
                                || "ROLE_CAREGIVER".equals(authority.getAuthority()));
    }

    public static void requireAdmin() {
        if (!isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }

    public static void requireCurrentUserOrAdmin(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User access is required");
        }

        Long currentUserId = getCurrentUser().getUserId();
        if (!userId.equals(currentUserId) && !isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this user");
        }
    }
}
