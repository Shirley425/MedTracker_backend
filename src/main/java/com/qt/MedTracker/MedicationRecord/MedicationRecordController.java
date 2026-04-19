package com.qt.MedTracker.MedicationRecord;

import com.qt.MedTracker.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medication-records")
public class MedicationRecordController {

    private final MedicationRecordService medicationRecordService;

    public MedicationRecordController(MedicationRecordService medicationRecordService) {
        this.medicationRecordService = medicationRecordService;
    }

    @GetMapping("/user/{userId}")
    public List<MedicationRecordResponse> getMedicationRecordsByUserId(@PathVariable Long userId) {
        SecurityUtils.requireCurrentUserOrAdmin(userId);
        return medicationRecordService.getMedicationRecordsByUserId(userId)
                .stream()
                .map(MedicationRecordResponse::from)
                .toList();
    }

    @GetMapping("/me")
    public List<MedicationRecordResponse> getMyMedicationRecords() {
        return medicationRecordService.getMedicationRecordsByUserId(SecurityUtils.getCurrentUser().getUserId())
                .stream()
                .map(MedicationRecordResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<MedicationRecordResponse> createMedicationRecord(@RequestBody MedicationRecordRequest request) {
        MedicationRecord savedRecord = medicationRecordService.createTakenRecord(
                SecurityUtils.getCurrentUser().getUserId(),
                request
        );
        return new ResponseEntity<>(MedicationRecordResponse.from(savedRecord), HttpStatus.CREATED);
    }
}
