package com.qt.MedTracker.SymptomLog;

import java.time.LocalDate;

public class SymptomLog {
    private Integer id;
    private Long User_id;
    private Integer Medication_id;
    private LocalDate date;
    private String symptom_name;
    private Integer severity_score;
    private String notes;

    public SymptomLog() {
    }

    public SymptomLog(Integer id,
                      Long user_id,
                      Integer medication_id,
                      LocalDate date,
                      String symptom_name,
                      Integer severity_score,
                      String notes) {
        this.id = id;
        User_id = user_id;
        Medication_id = medication_id;
        this.date = date;
        this.symptom_name = symptom_name;
        this.severity_score = severity_score;
        this.notes = notes;
    }

    public SymptomLog(Long user_id,
                      Integer medication_id,
                      LocalDate date,
                      String symptom_name,
                      Integer severity_score,
                      String notes) {
        User_id = user_id;
        Medication_id = medication_id;
        this.date = date;
        this.symptom_name = symptom_name;
        this.severity_score = severity_score;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUser_id() {
        return User_id;
    }

    public void setUser_id(Long user_id) {
        User_id = user_id;
    }

    public Integer getMedication_id() {
        return Medication_id;
    }

    public void setMedication_id(Integer medication_id) {
        Medication_id = medication_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSymptom_name() {
        return symptom_name;
    }

    public void setSymptom_name(String symptom_name) {
        this.symptom_name = symptom_name;
    }

    public Integer getSeverity_score() {
        return severity_score;
    }

    public void setSeverity_score(Integer severity_score) {
        this.severity_score = severity_score;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "SymptomLog{" +
                "id=" + id +
                ", User_id=" + User_id +
                ", Medication_id=" + Medication_id +
                ", date=" + date +
                ", symptom_name='" + symptom_name + '\'' +
                ", severity_score=" + severity_score +
                ", notes='" + notes + '\'' +
                '}';
    }
}
