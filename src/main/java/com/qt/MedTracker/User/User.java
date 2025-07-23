package com.qt.MedTracker.User;

import java.time.LocalDate;

public class User {
    private Long id;
    private String name;
    private Integer age;
    private LocalDate birth_date;
    private String email;
    private String role;
//    private String caregiver_for(nullable FK to User.id);

    public User() {
    }

    public User(Long id,
                String name,
                Integer age,
                LocalDate birth_date,
                String email,
                String role) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birth_date = birth_date;
        this.email = email;
        this.role = role;
    }

    public User(String name,
                Integer age,
                LocalDate birth_date,
                String email,
                String role) {
        this.name = name;
        this.age = age;
        this.birth_date = birth_date;
        this.email = email;
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
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                ", birth_date=" + birth_date +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
