package com.qt.MedTracker.VitalSign;

public class VitalSign {
    private Integer id;
    private Long User_id;
    private String date;
    private String body_temperature;
    private String heart_rate;
    private String blood_pressure;
    private String blood_sugar;

    public VitalSign() {
    }

    public VitalSign(String blood_sugar,
                     String blood_pressure,
                     String heart_rate,
                     String body_temperature,
                     String date,
                     Long user_id,
                     Integer id) {
        this.blood_sugar = blood_sugar;
        this.blood_pressure = blood_pressure;
        this.heart_rate = heart_rate;
        this.body_temperature = body_temperature;
        this.date = date;
        User_id = user_id;
        this.id = id;
    }

    public VitalSign(Long user_id,
                     String date,
                     String body_temperature,
                     String heart_rate,
                     String blood_pressure,
                     String blood_sugar) {
        User_id = user_id;
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
        return User_id;
    }

    public void setUser_id(Long user_id) {
        User_id = user_id;
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

    @Override
    public String toString() {
        return "VitalSign{" +
                "id=" + id +
                ", User_id=" + User_id +
                ", date='" + date + '\'' +
                ", body_temperature='" + body_temperature + '\'' +
                ", heart_rate='" + heart_rate + '\'' +
                ", blood_pressure='" + blood_pressure + '\'' +
                ", blood_sugar='" + blood_sugar + '\'' +
                '}';
    }
}
