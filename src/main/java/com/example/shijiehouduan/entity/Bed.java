package com.example.shijiehouduan.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 床位实体类
 */
public class Bed implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer bedId;
    private String roomNumber;
    private String bedNumber;
    private String status; // 空闲、占用、维修
    private BigDecimal pricePerDay;
    private Integer currentPatientId;
    
    // 关联的患者信息
    private Patient currentPatient;

    public Bed() {
    }

    public Integer getBedId() {
        return bedId;
    }

    public void setBedId(Integer bedId) {
        this.bedId = bedId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Integer getCurrentPatientId() {
        return currentPatientId;
    }

    public void setCurrentPatientId(Integer currentPatientId) {
        this.currentPatientId = currentPatientId;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public String toString() {
        return "Bed{" +
                "bedId=" + bedId +
                ", roomNumber='" + roomNumber + '\'' +
                ", bedNumber='" + bedNumber + '\'' +
                ", status='" + status + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", currentPatientId=" + currentPatientId +
                '}';
    }
} 