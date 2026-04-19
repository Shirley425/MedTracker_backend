package com.qt.MedTracker.MedicationRecord;

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
    public List<MedicationRecord> getMedicationRecordsByUserId(@PathVariable Long userId) {
        return medicationRecordService.getMedicationRecordsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<MedicationRecord> createMedicationRecord(@RequestBody MedicationRecord medicationRecord) {
        MedicationRecord savedRecord = medicationRecordService.createTakenRecord(medicationRecord);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }
}
