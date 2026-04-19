package com.qt.MedTracker.VitalSign;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vital_signs")
public class VitalSign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Long userId;
    private String date;
    private String body_temperature;
    private String heart_rate;
    private String blood_pressure;
    private String blood_sugar;

    public VitalSign() {
    }

    public VitalSign(Integer id,
                     Long user_id,
                     String date,
                     String body_temperature,
                     String heart_rate,
                     String blood_pressure,
                     String blood_sugar) {
        this.id = id;
        this.userId = user_id;
        this.date = date;
        this.body_temperature = body_temperature;
        this.heart_rate = heart_rate;
        this.blood_pressure = blood_pressure;
        this.blood_sugar = blood_sugar;
    }

    public VitalSign(Long user_id,
                     String date,
                     String body_temperature,
                     String heart_rate,
                     String blood_pressure,
                     String blood_sugar) {
        this.userId = user_id;
        this.date = date;
        this.body_temperature = body_temperature;
        this.heart_rate = heart_rate;
        this.blood_pressure = blood_pressure;
        this.blood_sugar = blood_sugar;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody_temperature() {
        return body_temperature;
    }

    public void setBody_temperature(String body_temperature) {
        this.body_temperature = body_temperature;
    }

    public String getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(String blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public String getBlood_sugar() {
        return blood_sugar;
    }

    public void setBlood_sugar(String blood_sugar) {
        this.blood_sugar = blood_sugar;
    }
}
