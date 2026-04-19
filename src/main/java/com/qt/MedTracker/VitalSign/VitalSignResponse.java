package com.qt.MedTracker.VitalSign;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VitalSignResponse(
        Integer id,
        @JsonProperty("user_id") Long userId,
        LocalDate date,
        @JsonProperty("body_temperature") BigDecimal bodyTemperature,
        @JsonProperty("heart_rate") Integer heartRate,
        @JsonProperty("blood_pressure") String bloodPressure,
        @JsonProperty("blood_pressure_systolic") Integer bloodPressureSystolic,
        @JsonProperty("blood_pressure_diastolic") Integer bloodPressureDiastolic,
        @JsonProperty("blood_sugar") Integer bloodSugar
) {
    public static VitalSignResponse from(VitalSign vitalSign) {
        return new VitalSignResponse(
                vitalSign.getId(),
                vitalSign.getUser_id(),
                vitalSign.getDate(),
                vitalSign.getBody_temperature(),
                vitalSign.getHeart_rate(),
                vitalSign.getBlood_pressure(),
                vitalSign.getBloodPressureSystolic(),
                vitalSign.getBloodPressureDiastolic(),
                vitalSign.getBlood_sugar()
        );
    }
}
