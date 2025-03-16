package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Patient;

/**
 * 患者服务接口
 */
public interface PatientService {
    /**
     * 根据ID查询患者
     */
    Patient getPatientById(Integer patientId);
    
    /**
     * 根据用户ID查询患者
     */
    Patient getPatientByUserId(Integer userId);
    
    /**
     * 根据身份证号查询患者
     */
    Patient getPatientByIdCard(String idCard);

    /**
     * 新增患者
     */
    boolean addPatient(Patient patient);
    
    /**
     * 更新患者
     */
    boolean updatePatient(Patient patient);
} 