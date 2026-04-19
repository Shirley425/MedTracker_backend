package com.qt.MedTracker.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Transient
    private Integer age;

//    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String slackMemberId;
    private String role;

    @ManyToOne
    @JoinColumn(name = "caregiver_for", referencedColumnName = "id", nullable = true)
    private User caregiverFor;


    public User() {
    }

    public User(Long id,
                String name,
                LocalDate birthDate,
                String email,
                String password,
                String role) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String name,
                LocalDate birthDate,
                String email,
                String password,
                String role) {
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSlackMemberId() {
        return slackMemberId;
    }

    public void setSlackMemberId(String slackMemberId) {
        this.slackMemberId = slackMemberId;
    }

    @Transient
    @com.fasterxml.jackson.annotation.JsonProperty("slack_connected")
    public boolean isSlackConnected() {
        return slackMemberId != null && !slackMemberId.isBlank();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
