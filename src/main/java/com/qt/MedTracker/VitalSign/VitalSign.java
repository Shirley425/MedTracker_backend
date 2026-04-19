package com.qt.MedTracker.VitalSign;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vital_signs")
public class VitalSign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Long userId;
    @Column(name = "recorded_on")
    private LocalDate date;
    @Column(name = "body_temperature_c")
    private BigDecimal bodyTemperature;
    @Column(name = "heart_rate_bpm")
    private Integer heartRate;
    @Column(name = "blood_pressure_systolic")
    private Integer bloodPressureSystolic;
    @Column(name = "blood_pressure_diastolic")
    private Integer bloodPressureDiastolic;
    @Column(name = "blood_sugar_mg_dl")
    private Integer bloodSugar;

    public VitalSign() {
    }

    public VitalSign(Integer id,
                     Long user_id,
                     LocalDate date,
                     BigDecimal bodyTemperature,
                     Integer heartRate,
                     Integer bloodPressureSystolic,
                     Integer bloodPressureDiastolic,
                     Integer bloodSugar) {
        this.id = id;
        this.userId = user_id;
        this.date = date;
        this.bodyTemperature = bodyTemperature;
        this.heartRate = heartRate;
        this.bloodPressureSystolic = bloodPressureSystolic;
        this.bloodPressureDiastolic = bloodPressureDiastolic;
        this.bloodSugar = bloodSugar;
    }

    public VitalSign(Long user_id,
                     LocalDate date,
                     BigDecimal bodyTemperature,
                     Integer heartRate,
                     Integer bloodPressureSystolic,
                     Integer bloodPressureDiastolic,
                     Integer bloodSugar) {
        this.userId = user_id;
        this.date = date;
        this.bodyTemperature = bodyTemperature;
        this.heartRate = heartRate;
        this.bloodPressureSystolic = bloodPressureSystolic;
        this.bloodPressureDiastolic = bloodPressureDiastolic;
        this.bloodSugar = bloodSugar;
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

    public void setUser_id(Long user_id) {
        this.userId = user_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getBody_temperature() {
        return bodyTemperature;
    }

    public void setBody_temperature(BigDecimal bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public Integer getHeart_rate() {
        return heartRate;
    }

    public void setHeart_rate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    @Transient
    public String getBlood_pressure() {
        if (bloodPressureSystolic == null || bloodPressureDiastolic == null) {
            return null;
        }
        return bloodPressureSystolic + "/" + bloodPressureDiastolic;
    }

    public void setBlood_pressure(String bloodPressure) {
        if (bloodPressure == null || bloodPressure.isBlank()) {
            this.bloodPressureSystolic = null;
            this.bloodPressureDiastolic = null;
            return;
        }

        String[] parts = bloodPressure.split("/");
        this.bloodPressureSystolic = parts.length > 0 ? Integer.valueOf(parts[0].trim()) : null;
        this.bloodPressureDiastolic = parts.length > 1 ? Integer.valueOf(parts[1].trim()) : null;
    }

    public Integer getBlood_sugar() {
        return bloodSugar;
    }

    public void setBlood_sugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Integer getBloodPressureSystolic() {
        return bloodPressureSystolic;
    }

    public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
        this.bloodPressureSystolic = bloodPressureSystolic;
    }

    public Integer getBloodPressureDiastolic() {
        return bloodPressureDiastolic;
    }

    public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
        this.bloodPressureDiastolic = bloodPressureDiastolic;
    }
}
