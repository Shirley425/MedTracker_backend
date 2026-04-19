package com.qt.MedTracker.Medication;

import com.qt.MedTracker.SlackAPI.SlackService;
import com.qt.MedTracker.User.User;
import com.qt.MedTracker.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final UserService userService;
    private final SlackService slackService;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository,
                             UserService userService,
                             SlackService slackService) {
        this.medicationRepository = medicationRepository;
        this.userService = userService;
        this.slackService = slackService;
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

    public Medication saveMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateSlackNotifications(Integer id, boolean enabled) {
        Medication medication = getMedicationOrThrow(id);
        medication.setSlack_notifications_enabled(enabled);
        return medicationRepository.save(medication);
    }

    public void sendSlackReminder(Integer id) {
        Medication medication = getMedicationOrThrow(id);

        if (!medication.isSlack_notifications_enabled()) {
            throw new IllegalStateException("Slack notifications are disabled for this medication");
        }

        User user = userService.getUserById(medication.getUser_id());

        if (!user.isSlackConnected()) {
            throw new IllegalStateException("User has not connected Slack yet");
        }

        slackService.sendMedicationReminder(
                user.getSlackWebhookUrl(),
                user.getName(),
                medication.getId(),
                medication.getName(),
                medication.getDosage(),
                medication.getFrequency()
        );
    }

    public void deleteMedication(Integer id) {
        medicationRepository.deleteById(id);
    }
}
