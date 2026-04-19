package com.qt.MedTracker.Medication;

import com.qt.MedTracker.User.User;
import com.qt.MedTracker.User.UserService;
import com.qt.MedTracker.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MedicationService medicationService;

    @Test
    void sendSlackReminderRequiresNotificationsToBeEnabled() {
        Medication medication = new Medication(1, 1L, "Ibuprofen", "200mg", "Twice daily",
                LocalDate.now(), LocalDate.now().plusDays(7), "Take with food");
        medication.setSlack_notifications_enabled(false);
        when(medicationRepository.findByIdAndUserId(1, 1L)).thenReturn(Optional.of(medication));

        assertThatThrownBy(() -> medicationService.sendSlackReminder(1L, 1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("disabled");
    }

    @Test
    void sendSlackReminderSendsNotificationForConnectedUser() {
        Medication medication = new Medication(1, 1L, "Ibuprofen", "200mg", "Twice daily",
                LocalDate.now(), LocalDate.now().plusDays(7), "Take with food");
        medication.setSlack_notifications_enabled(true);

        User user = new User(1L, "Joe", LocalDate.of(1994, 6, 15), "joe@example.com", "encoded", "PATIENT");
        user.setSlackMemberId("U12345678");

        when(medicationRepository.findByIdAndUserId(1, 1L)).thenReturn(Optional.of(medication));
        when(userService.getUserById(1L)).thenReturn(user);

        medicationService.sendSlackReminder(1L, 1);

        verify(notificationService).sendMedicationReminder(user, medication);
    }
}
