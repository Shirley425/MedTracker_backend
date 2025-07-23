package com.qt.MedTracker.Medication;

import java.time.LocalDate;

public class Medication {
    private Integer id;
    private Long User_id;
    private String name;
    private String dosage;
    private String frequency;
    private LocalDate start_date;
    private LocalDate end_date;
    private String note;
//    public long external_id; (RxNorm/ openFDA)


    public Medication() {
    }

    public Medication(Integer id,
                      Long user_id,
                      String name,
                      String dosage,
                      String frequency,
                      LocalDate start_date,
                      LocalDate end_date,
                      String note) {
        this.id = id;
        User_id = user_id;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.start_date = start_date;
        this.end_date = end_date;
        this.note = note;
    }

    public Medication(Long user_id,
                      String name,
                      String dosage,
                      String frequency,
                      LocalDate start_date,
                      LocalDate end_date,
                      String note) {
        User_id = user_id;
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.start_date = start_date;
        this.end_date = end_date;
        this.note = note;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "id=" + id +
                ", User_id=" + User_id +
                ", name='" + name + '\'' +
                ", dosage='" + dosage + '\'' +
                ", frequency='" + frequency + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", note='" + note + '\'' +
                '}';
    }
}
