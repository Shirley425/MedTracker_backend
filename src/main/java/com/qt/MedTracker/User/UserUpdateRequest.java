package com.qt.MedTracker.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserUpdateRequest(
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be 120 characters or fewer")
        String name,
        @NotNull(message = "birthDate is required")
        @Past(message = "birthDate must be in the past")
        LocalDate birthDate,
        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email
) {
}
