package com.example.shijiehouduan.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 药品实体类
 */
public class Medicine {
    private Integer medicineId;     // 药品ID
    private String name;            // 药品名称
    private String specification;   // 规格
    private String unit;            // 单位
    private String manufacturer;    // 生产厂家
    private Integer stock;          // 库存
    private BigDecimal price;       // 价格
    private Date expiryDate;        // 有效期
    private Date createdAt;         // 创建时间

    // 构造方法
    public Medicine() {
    }

    // Getters and Setters
    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", name='" + name + '\'' +
                ", specification='" + specification + '\'' +
                ", unit='" + unit + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", expiryDate=" + expiryDate +
                ", createdAt=" + createdAt +
                '}';
    }
} 