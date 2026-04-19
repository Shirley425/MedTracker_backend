package com.qt.MedTracker.Medication;

import com.qt.MedTracker.SlackAPI.SlackService;
import com.qt.MedTracker.SlackAPI.SlackNotificationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;
    private final SlackService slackService;

    public MedicationController(MedicationService medicationService, SlackService slackService) {
        this.medicationService = medicationService;
        this.slackService = slackService;
    }

    @GetMapping
    public List<Medication> getAllMedications() {
        return medicationService.getAllMedications();
    }

    @GetMapping("/user/{userId}")
    public List<Medication> getMedicationsByUserId(@PathVariable Long userId) {
        return medicationService.getMedicationsByUserId(userId);
    }

    @GetMapping("/{id}")
    public Optional<Medication> getMedicationById(@PathVariable Integer id) {
        return medicationService.getMedicationById(id);
    }


    @PutMapping("/{id}")
    public Medication updateMedication(@PathVariable Integer id, @RequestBody Medication medication) {
        medication.setId(id);
        return medicationService.saveMedication(medication);
    }

    @DeleteMapping("/{id}")
    public void deleteMedication(@PathVariable Integer id) {
        medicationService.deleteMedication(id);
    }

    @PutMapping("/{id}/slack-notifications")
    public Medication updateSlackNotifications(@PathVariable Integer id,
                                               @RequestBody SlackNotificationRequest request) {
        return medicationService.updateSlackNotifications(id, request.isEnabled());
    }

    @PostMapping("/{id}/slack-reminder")
    public ResponseEntity<Void> sendSlackReminder(@PathVariable Integer id) {
        medicationService.sendSlackReminder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Medication> addMedication(@RequestBody Medication med) {
        Medication saved = medicationService.saveMedication(med);

        slackService.sendSlackMessage("New medication logged: " + med.getName());

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}
