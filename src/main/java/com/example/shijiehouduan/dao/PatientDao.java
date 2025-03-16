package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Patient;

/**
 * 患者DAO接口
 */
public interface PatientDao {
    /**
     * 根据ID查询患者
     */
    Patient findById(Integer patientId);

    /**
     * 根据用户ID查询患者
     */
    Patient findByUserId(Integer userId);

    /**
     * 根据身份证号查询患者
     */
    Patient findByIdCard(String idCard);

    /**
     * 新增患者
     */
    int insert(Patient patient);

    /**
     * 更新患者
     */
    int update(Patient patient);
} 