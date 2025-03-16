package com.example.shijiehouduan.entity;

import java.math.BigDecimal;

/**
 * 处方药品实体类
 */
public class PrescriptionMedicine {
    private Integer id;                 // 主键ID
    private Integer prescriptionId;     // 处方ID
    private Integer medicineId;         // 药品ID
    private Integer quantity;           // 数量
    private String usageMethod;         // 用法
    private String dosage;              // 剂量
    private BigDecimal fee;             // 费用
    
    // 关联药品信息
    private Medicine medicine;

    // 构造方法
    public PrescriptionMedicine() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUsageMethod() {
        return usageMethod;
    }

    public void setUsageMethod(String usageMethod) {
        this.usageMethod = usageMethod;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    @Override
    public String toString() {
        return "PrescriptionMedicine{" +
                "id=" + id +
                ", prescriptionId=" + prescriptionId +
                ", medicineId=" + medicineId +
                ", quantity=" + quantity +
                ", usageMethod='" + usageMethod + '\'' +
                ", dosage='" + dosage + '\'' +
                ", fee=" + fee +
                '}';
    }
} 