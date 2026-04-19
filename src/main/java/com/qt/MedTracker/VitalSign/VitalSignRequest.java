package com.qt.MedTracker.VitalSign;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VitalSignRequest(
        LocalDate date,
        @JsonProperty("body_temperature") BigDecimal bodyTemperature,
        @JsonProperty("heart_rate") Integer heartRate,
        @JsonProperty("blood_pressure_systolic") Integer bloodPressureSystolic,
        @JsonProperty("blood_pressure_diastolic") Integer bloodPressureDiastolic,
        @JsonProperty("blood_sugar") Integer bloodSugar
) {
}
