package com.qt.MedTracker.User;

import com.qt.MedTracker.User.User;
import com.qt.MedTracker.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@Configuration
public class UserConfig {
    public static final String JOE_EMAIL = "joe@example.com";
    public static final String ADMIN_EMAIL = "admin@example.com";
    public static final String JOE_PASSWORD = "joe123";
    public static final String ADMIN_PASSWORD = "admin123";

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            upsertSeedUser(
                    userRepository,
                    passwordEncoder,
                    "Joe Doe",
                    LocalDate.of(1994, Month.JUNE, 15),
                    JOE_EMAIL,
                    JOE_PASSWORD,
                    "PATIENT"
            );

            upsertSeedUser(
                    userRepository,
                    passwordEncoder,
                    "Admin User",
                    LocalDate.of(1989, Month.APRIL, 5),
                    ADMIN_EMAIL,
                    ADMIN_PASSWORD,
                    "CAREGIVER"
            );
        };
    }

    private void upsertSeedUser(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                String name,
                                LocalDate birthDate,
                                String email,
                                String rawPassword,
                                String role) {
        Optional<User> existingUser = userRepository.findUserByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(name);
            user.setBirthDate(birthDate);
            user.setRole(role);

            if (user.getPassword() == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(rawPassword));
            }

            userRepository.save(user);
            return;
        }

        User user = new User(
                name,
                birthDate,
                email,
                passwordEncoder.encode(rawPassword),
                role
        );

        userRepository.save(user);
    }
}
