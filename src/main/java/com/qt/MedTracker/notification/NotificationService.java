package com.qt.MedTracker.notification;

import com.qt.MedTracker.Medication.Medication;
import com.qt.MedTracker.User.User;

public interface NotificationService {
    void sendMedicationReminder(User user, Medication medication);
}
