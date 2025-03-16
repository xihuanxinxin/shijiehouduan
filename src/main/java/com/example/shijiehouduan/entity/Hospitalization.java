package com.example.shijiehouduan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 住院记录实体类
 */
public class Hospitalization implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer hospitalizationId;
    private Integer patientId;
    private Integer doctorId;
    private Date admissionDate;
    private Date dischargeDate;
    private Integer bedId;
    private String reason;
    private String status; // 在院、出院、转院、死亡
    private Double totalFee;
    private String remarks;
    private Date createdAt;
    
    // 关联的患者信息
    private Patient patient;
    
    // 关联的医生信息
    private Doctor doctor;
    
    // 关联的床位信息
    private Bed bed;

    public Hospitalization() {
    }

    public Integer getHospitalizationId() {
        return hospitalizationId;
    }

    public void setHospitalizationId(Integer hospitalizationId) {
        this.hospitalizationId = hospitalizationId;
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

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public Integer getBedId() {
        return bedId;
    }

    public void setBedId(Integer bedId) {
        this.bedId = bedId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public Bed getBed() {
        return bed;
    }

    public void setBed(Bed bed) {
        this.bed = bed;
    }

    @Override
    public String toString() {
        return "Hospitalization{" +
                "hospitalizationId=" + hospitalizationId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", admissionDate=" + admissionDate +
                ", dischargeDate=" + dischargeDate +
                ", bedId=" + bedId +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", totalFee=" + totalFee +
                ", remarks='" + remarks + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 