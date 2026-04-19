package com.qt.MedTracker.MedicationRecord;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record MedicationRecordResponse(
        Integer id,
        @JsonProperty("user_id") Long userId,
        @JsonProperty("medication_id") Integer medicationId,
        @JsonProperty("taken_at") LocalDateTime takenAt,
        String status,
        String source
) {
    public static MedicationRecordResponse from(MedicationRecord record) {
        return new MedicationRecordResponse(
                record.getId(),
                record.getUser_id(),
                record.getMedication_id(),
                record.getTaken_at(),
                record.getStatus() == null ? MedicationRecordStatus.TAKEN.name() : record.getStatus().name(),
                record.getSource() == null ? MedicationRecordSource.MANUAL.name() : record.getSource().name()
        );
    }
}
