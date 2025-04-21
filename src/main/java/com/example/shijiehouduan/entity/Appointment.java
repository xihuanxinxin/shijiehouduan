package com.example.shijiehouduan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 预约挂号实体类
 */
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer appointmentId;
    private Integer patientId;
    private Integer doctorId;
    private Date appointmentDate;
    private String timeSlot;
    private String appointmentType; // 初诊、复诊、检查、手术前、手术后
    private Double fee;
    private String status; // 待确认、已确认、已完成、已取消
    private Date createdAt;
    
    // 关联的患者信息
    private Patient patient;
    
    // 关联的医生信息
    private Doctor doctor;
    
    // 支付状态
    private String paymentStatus; // 待支付、已支付、已退款、部分退款

    public Appointment() {
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentDate=" + appointmentDate +
                ", timeSlot='" + timeSlot + '\'' +
                ", appointmentType='" + appointmentType + '\'' +
                ", fee=" + fee +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
} 