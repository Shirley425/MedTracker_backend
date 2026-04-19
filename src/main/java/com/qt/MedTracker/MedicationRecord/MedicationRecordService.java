package com.qt.MedTracker.MedicationRecord;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicationRecordService {

    private final MedicationRecordRepository medicationRecordRepository;

    public MedicationRecordService(MedicationRecordRepository medicationRecordRepository) {
        this.medicationRecordRepository = medicationRecordRepository;
    }

    public List<MedicationRecord> getMedicationRecordsByUserId(Long userId) {
        return medicationRecordRepository.findByUserIdOrderByTakenAtDesc(userId);
    }

    public MedicationRecord createTakenRecord(MedicationRecord medicationRecord) {
        if (medicationRecord.getTaken_at() == null) {
            medicationRecord.setTaken_at(LocalDateTime.now());
        }

        if (medicationRecord.getStatus() == null || medicationRecord.getStatus().isBlank()) {
            medicationRecord.setStatus("TAKEN");
        }

        return medicationRecordRepository.save(medicationRecord);
    }
}
