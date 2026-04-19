package com.qt.MedTracker.MedicationRecord;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MedicationRecordRequest(
        @JsonProperty("medication_id") Integer medicationId,
        String status,
        String source
) {
}
