package com.qt.MedTracker.MedicationRecord;

import com.qt.MedTracker.Medication.MedicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicationRecordService {

    private final MedicationRecordRepository medicationRecordRepository;
    private final MedicationService medicationService;

    public MedicationRecordService(MedicationRecordRepository medicationRecordRepository,
                                   MedicationService medicationService) {
        this.medicationRecordRepository = medicationRecordRepository;
        this.medicationService = medicationService;
    }

    public List<MedicationRecord> getMedicationRecordsByUserId(Long userId) {
        return medicationRecordRepository.findByUserIdOrderByTakenAtDesc(userId);
    }

    public MedicationRecord createTakenRecord(Long userId, MedicationRecordRequest request) {
        medicationService.getMedicationForUserOrThrow(request.medicationId(), userId);

        MedicationRecord medicationRecord = new MedicationRecord();
        medicationRecord.setUser_id(userId);
        medicationRecord.setMedication_id(request.medicationId());
        medicationRecord.setTaken_at(LocalDateTime.now());
        medicationRecord.setStatus(parseStatus(request.status()));
        medicationRecord.setSource(parseSource(request.source()));

        return medicationRecordRepository.save(medicationRecord);
    }

    private MedicationRecordStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return MedicationRecordStatus.TAKEN;
        }
        return MedicationRecordStatus.valueOf(status.trim().toUpperCase());
    }

    private MedicationRecordSource parseSource(String source) {
        if (source == null || source.isBlank()) {
            return MedicationRecordSource.MANUAL;
        }
        return MedicationRecordSource.valueOf(source.trim().toUpperCase());
    }
}
