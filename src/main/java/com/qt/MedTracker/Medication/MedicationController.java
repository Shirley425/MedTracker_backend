package com.qt.MedTracker.Medication;

import com.qt.MedTracker.SlackAPI.SlackNotificationRequest;
import com.qt.MedTracker.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public List<MedicationResponse> getAllMedications() {
        SecurityUtils.requireAdmin();
        return medicationService.getAllMedications().stream().map(MedicationResponse::from).toList();
    }

    @GetMapping("/user/{userId}")
    public List<MedicationResponse> getMedicationsByUserId(@PathVariable Long userId) {
        SecurityUtils.requireCurrentUserOrAdmin(userId);
        return medicationService.getMedicationsByUserId(userId).stream().map(MedicationResponse::from).toList();
    }

    @GetMapping("/me")
    public List<MedicationResponse> getMyMedications() {
        return medicationService.getMedicationsByUserId(SecurityUtils.getCurrentUser().getUserId())
                .stream()
                .map(MedicationResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponse> getMedicationById(@PathVariable Integer id) {
        Medication medication = medicationService.getAccessibleMedication(
                id,
                SecurityUtils.getCurrentUser().getUserId(),
                SecurityUtils.isAdmin()
        );
        return ResponseEntity.ok(MedicationResponse.from(medication));
    }


    @PutMapping("/{id}")
    public MedicationResponse updateMedication(@PathVariable Integer id, @Valid @RequestBody MedicationRequest request) {
        Medication medication = medicationService.getAccessibleMedication(
                id,
                SecurityUtils.getCurrentUser().getUserId(),
                SecurityUtils.isAdmin()
        );
        medication.setName(request.name());
        medication.setDosage(request.dosage());
        medication.setFrequency(request.frequency());
        medication.setStart_date(request.startDate());
        medication.setEnd_date(request.endDate());
        medication.setNote(request.note());
        return MedicationResponse.from(medicationService.saveMedication(medication));
    }

    @DeleteMapping("/{id}")
    public void deleteMedication(@PathVariable Integer id) {
        medicationService.deleteMedicationForUser(
                id,
                SecurityUtils.getCurrentUser().getUserId(),
                SecurityUtils.isAdmin()
        );
    }

    @PutMapping("/{id}/slack-notifications")
    public MedicationResponse updateSlackNotifications(@PathVariable Integer id,
                                                       @RequestBody SlackNotificationRequest request) {
        return MedicationResponse.from(
                medicationService.updateSlackNotifications(
                        SecurityUtils.getCurrentUser().getUserId(),
                        id,
                        request.isEnabled()
                )
        );
    }

    @PostMapping("/{id}/slack-reminder")
    public ResponseEntity<Void> sendSlackReminder(@PathVariable Integer id) {
        medicationService.sendSlackReminder(SecurityUtils.getCurrentUser().getUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<MedicationResponse> addMedication(@Valid @RequestBody MedicationRequest request) {
        Medication saved = medicationService.createMedication(SecurityUtils.getCurrentUser().getUserId(), request);
        return new ResponseEntity<>(MedicationResponse.from(saved), HttpStatus.CREATED);
    }

}
