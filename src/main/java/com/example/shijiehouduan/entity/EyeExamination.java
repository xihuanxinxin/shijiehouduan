package com.example.shijiehouduan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 眼科检查记录实体类
 */
public class EyeExamination implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer examinationId;
    private Integer recordId;
    private Integer patientId;
    private Integer doctorId;
    private Date examinationDate;
    private String visualAcuityLeft;
    private String visualAcuityRight;
    private String intraocularPressureLeft;
    private String intraocularPressureRight;
    private String fundusExamination;
    private String otherResults;
    private Double fee;
    private Date createdAt;
    
    // 关联的病历信息
    private MedicalRecord medicalRecord;
    
    // 关联的患者信息
    private Patient patient;
    
    // 关联的医生信息
    private Doctor doctor;

    public EyeExamination() {
    }

    public Integer getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(Integer examinationId) {
        this.examinationId = examinationId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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

    public Date getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(Date examinationDate) {
        this.examinationDate = examinationDate;
    }

    public String getVisualAcuityLeft() {
        return visualAcuityLeft;
    }

    public void setVisualAcuityLeft(String visualAcuityLeft) {
        this.visualAcuityLeft = visualAcuityLeft;
    }

    public String getVisualAcuityRight() {
        return visualAcuityRight;
    }

    public void setVisualAcuityRight(String visualAcuityRight) {
        this.visualAcuityRight = visualAcuityRight;
    }

    public String getIntraocularPressureLeft() {
        return intraocularPressureLeft;
    }

    public void setIntraocularPressureLeft(String intraocularPressureLeft) {
        this.intraocularPressureLeft = intraocularPressureLeft;
    }

    public String getIntraocularPressureRight() {
        return intraocularPressureRight;
    }

    public void setIntraocularPressureRight(String intraocularPressureRight) {
        this.intraocularPressureRight = intraocularPressureRight;
    }

    public String getFundusExamination() {
        return fundusExamination;
    }

    public void setFundusExamination(String fundusExamination) {
        this.fundusExamination = fundusExamination;
    }

    public String getOtherResults() {
        return otherResults;
    }

    public void setOtherResults(String otherResults) {
        this.otherResults = otherResults;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
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
        return "EyeExamination{" +
                "examinationId=" + examinationId +
                ", recordId=" + recordId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", examinationDate=" + examinationDate +
                ", visualAcuityLeft='" + visualAcuityLeft + '\'' +
                ", visualAcuityRight='" + visualAcuityRight + '\'' +
                ", intraocularPressureLeft='" + intraocularPressureLeft + '\'' +
                ", intraocularPressureRight='" + intraocularPressureRight + '\'' +
                ", fundusExamination='" + fundusExamination + '\'' +
                ", otherResults='" + otherResults + '\'' +
                ", fee=" + fee +
                ", createdAt=" + createdAt +
                '}';
    }
} 