package com.example.shijiehouduan.entity;

import java.util.Date;

/**
 * 处方实体类
 */
public class Prescription {
    private Integer prescriptionId;  // 处方ID
    private Integer recordId;        // 病历ID
    private Integer patientId;       // 患者ID
    private Integer doctorId;        // 医生ID
    private Date issueDate;          // 开具日期
    private String diagnosis;        // 诊断
    private Double totalFee;         // 总费用
    private String status;           // 状态（待发药、已发药、已取消）
    private String remarks;          // 备注
    private Date createdAt;          // 创建时间
    
    // 关联属性
    private Patient patient;         // 患者信息
    private Doctor doctor;           // 医生信息

    public Prescription() {
    }

    public Prescription(Integer prescriptionId, Integer recordId, Integer patientId, Integer doctorId, 
                        Date issueDate, String diagnosis, Double totalFee, String status, 
                        String remarks, Date createdAt) {
        this.prescriptionId = prescriptionId;
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.issueDate = issueDate;
        this.diagnosis = diagnosis;
        this.totalFee = totalFee;
        this.status = status;
        this.remarks = remarks;
        this.createdAt = createdAt;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
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

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
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
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", recordId=" + recordId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", issueDate=" + issueDate +
                ", diagnosis='" + diagnosis + '\'' +
                ", totalFee=" + totalFee +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 