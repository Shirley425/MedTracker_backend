package com.qt.MedTracker.Medication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MedicationRequest(
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be 120 characters or fewer")
        String name,
        @Size(max = 60, message = "dosage must be 60 characters or fewer")
        String dosage,
        @Size(max = 80, message = "frequency must be 80 characters or fewer")
        String frequency,
        @JsonProperty("start_date") LocalDate startDate,
        @JsonProperty("end_date") LocalDate endDate,
        @Size(max = 500, message = "note must be 500 characters or fewer")
        String note
) {
    @AssertTrue(message = "end_date must be on or after start_date")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
