package com.qt.MedTracker.Medication;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record MedicationResponse(
        Integer id,
        @JsonProperty("user_id") Long userId,
        String name,
        String dosage,
        String frequency,
        @JsonProperty("start_date") LocalDate startDate,
        @JsonProperty("end_date") LocalDate endDate,
        String note,
        @JsonProperty("slack_notifications_enabled") boolean slackNotificationsEnabled
) {
    public static MedicationResponse from(Medication medication) {
        return new MedicationResponse(
                medication.getId(),
                medication.getUser_id(),
                medication.getName(),
                medication.getDosage(),
                medication.getFrequency(),
                medication.getStart_date(),
                medication.getEnd_date(),
                medication.getNote(),
                medication.isSlack_notifications_enabled()
        );
    }
}
