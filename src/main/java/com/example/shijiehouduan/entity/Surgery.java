package com.example.shijiehouduan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 手术记录实体类
 */
public class Surgery implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer surgeryId;
    private Integer patientId;
    private Integer doctorId;
    private Integer recordId;
    private String surgeryType;
    private Date surgeryDate;
    private String description;
    private String preEvaluation;
    private String postEvaluation;
    private Double fee;
    private String status; // 待手术、手术中、已完成、已取消
    private String remarks;
    private Date createdAt;
    
    // 关联的病历信息
    private MedicalRecord medicalRecord;
    
    // 关联的患者信息
    private Patient patient;
    
    // 关联的医生信息
    private Doctor doctor;

    public Surgery() {
    }

    public Integer getSurgeryId() {
        return surgeryId;
    }

    public void setSurgeryId(Integer surgeryId) {
        this.surgeryId = surgeryId;
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

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    public Date getSurgeryDate() {
        return surgeryDate;
    }

    public void setSurgeryDate(Date surgeryDate) {
        this.surgeryDate = surgeryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreEvaluation() {
        return preEvaluation;
    }

    public void setPreEvaluation(String preEvaluation) {
        this.preEvaluation = preEvaluation;
    }

    public String getPostEvaluation() {
        return postEvaluation;
    }

    public void setPostEvaluation(String postEvaluation) {
        this.postEvaluation = postEvaluation;
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

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
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

    @Override
    public String toString() {
        return "Surgery{" +
                "surgeryId=" + surgeryId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", recordId=" + recordId +
                ", surgeryType='" + surgeryType + '\'' +
                ", surgeryDate=" + surgeryDate +
                ", description='" + description + '\'' +
                ", preEvaluation='" + preEvaluation + '\'' +
                ", postEvaluation='" + postEvaluation + '\'' +
                ", fee=" + fee +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 