package com.example.shijiehouduan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 医生实体类
 */
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer doctorId;
    private Integer userId;
    private String specialty;
    private String title;
    private String introduction;
    private String schedule;
    private Date createdAt;
    
    // 关联的用户信息
    private User user;

    public Doctor() {
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", userId=" + userId +
                ", specialty='" + specialty + '\'' +
                ", title='" + title + '\'' +
                ", introduction='" + introduction + '\'' +
                ", schedule='" + schedule + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 