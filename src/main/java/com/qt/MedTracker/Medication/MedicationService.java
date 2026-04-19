package com.qt.MedTracker.Medication;

import com.qt.MedTracker.User.User;
import com.qt.MedTracker.User.UserService;
import com.qt.MedTracker.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository,
                             UserService userService,
                             NotificationService notificationService) {
        this.medicationRepository = medicationRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public List<Medication> getMedicationsByUserId(Long userId) {
        return medicationRepository.findByUserId(userId);
    }

    public Optional<Medication> getMedicationById(Integer id) {
        return medicationRepository.findById(id);
    }

    public Medication getMedicationOrThrow(Integer id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("medication with id " + id + " does not exist"));
    }

    public Medication getMedicationForUserOrThrow(Integer id, Long userId) {
        return medicationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalStateException("medication with id " + id + " does not exist for this user"));
    }

    public Medication getAccessibleMedication(Integer id, Long currentUserId, boolean isAdmin) {
        return isAdmin ? getMedicationOrThrow(id) : getMedicationForUserOrThrow(id, currentUserId);
    }

    public Medication saveMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication createMedication(Long userId, MedicationRequest request) {
        Medication medication = new Medication(
                userId,
                request.name(),
                request.dosage(),
                request.frequency(),
                request.startDate(),
                request.endDate(),
                request.note()
        );
        return medicationRepository.save(medication);
    }

    public Medication updateSlackNotifications(Long userId, Integer id, boolean enabled) {
        Medication medication = getMedicationForUserOrThrow(id, userId);
        medication.setSlack_notifications_enabled(enabled);
        return medicationRepository.save(medication);
    }

    public void sendSlackReminder(Long userId, Integer id) {
        Medication medication = getMedicationForUserOrThrow(id, userId);

        if (!medication.isSlack_notifications_enabled()) {
            throw new IllegalStateException("Slack notifications are disabled for this medication");
        }

        User user = userService.getUserById(medication.getUser_id());

        if (!user.isSlackConnected()) {
            throw new IllegalStateException("User has not connected Slack yet");
        }

        notificationService.sendMedicationReminder(user, medication);
    }

    public void deleteMedication(Integer id) {
        medicationRepository.deleteById(id);
    }

    public void deleteMedicationForUser(Integer id, Long currentUserId, boolean isAdmin) {
        Medication medication = getAccessibleMedication(id, currentUserId, isAdmin);
        medicationRepository.deleteById(medication.getId());
    }
}
