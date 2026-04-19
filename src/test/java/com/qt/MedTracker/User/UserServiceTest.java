package com.qt.MedTracker.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUserNormalizesEmailAndEncodesPassword() {
        User user = new User("Joe", LocalDate.of(1994, 6, 15), " Joe@Example.com ", "secret123", "PATIENT");
        when(userRepository.findUserByEmail("joe@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.saveUser(user);

        assertThat(saved.getEmail()).isEqualTo("joe@example.com");
        assertThat(saved.getPassword()).isEqualTo("encoded-secret");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("joe@example.com");
    }

    @Test
    void updateSlackMemberIdRejectsDuplicateLink() {
        User currentUser = new User(1L, "Joe", LocalDate.of(1994, 6, 15), "joe@example.com", "encoded", "PATIENT");
        User existingUser = new User(2L, "Admin", LocalDate.of(1989, 4, 5), "admin@example.com", "encoded", "ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(userRepository.findBySlackMemberId("U12345678")).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userService.updateSlackMemberId(1L, " U12345678 "))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already linked");
    }

    @Test
    void authenticateRejectsInvalidPassword() {
        User user = new User(1L, "Joe", LocalDate.of(1994, 6, 15), "joe@example.com", "encoded", "PATIENT");
        when(userRepository.findUserByEmail("joe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded")).thenReturn(false);

        assertThatThrownBy(() -> userService.authenticate("joe@example.com", "wrong-password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email or password");
    }
}
