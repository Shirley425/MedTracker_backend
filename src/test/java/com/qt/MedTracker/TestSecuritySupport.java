package com.qt.MedTracker;

import com.qt.MedTracker.security.AppUserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public final class TestSecuritySupport {

    private TestSecuritySupport() {
    }

    public static void authenticatePatient() {
        authenticate(new AppUserPrincipal(1L, "joe@example.com", "PATIENT"));
    }

    public static void authenticateAdmin() {
        authenticate(new AppUserPrincipal(2L, "admin@example.com", "ADMIN"));
    }

    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    private static void authenticate(AppUserPrincipal principal) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );
    }
}
