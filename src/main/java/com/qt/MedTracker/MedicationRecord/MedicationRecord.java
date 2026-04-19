package com.qt.MedTracker.MedicationRecord;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "medication_records")
public class MedicationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Long userId;

    @Column(name = "medication_id")
    @JsonProperty("medication_id")
    private Integer medicationId;

    @Column(name = "taken_at")
    @JsonProperty("taken_at")
    private LocalDateTime takenAt;

    private String status;

    public MedicationRecord() {
    }

    public MedicationRecord(Long userId, Integer medicationId, LocalDateTime takenAt, String status) {
        this.userId = userId;
        this.medicationId = medicationId;
        this.takenAt = takenAt;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUser_id() {
        return userId;
    }

    public void setUser_id(Long userId) {
        this.userId = userId;
    }

    public Integer getMedication_id() {
        return medicationId;
    }

    public void setMedication_id(Integer medicationId) {
        this.medicationId = medicationId;
    }

    public LocalDateTime getTaken_at() {
        return takenAt;
    }

    public void setTaken_at(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
