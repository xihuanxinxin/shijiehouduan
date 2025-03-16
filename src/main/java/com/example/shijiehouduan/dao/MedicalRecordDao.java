package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.MedicalRecord;
import java.util.List;

/**
 * 病历DAO接口
 */
public interface MedicalRecordDao {
    /**
     * 根据ID查询病历
     */
    MedicalRecord findById(Integer recordId);

    /**
     * 根据患者ID查询病历列表
     */
    List<MedicalRecord> findByPatientId(Integer patientId);

    /**
     * 根据医生ID查询病历列表
     */
    List<MedicalRecord> findByDoctorId(Integer doctorId);

    /**
     * 根据状态查询病历列表
     */
    List<MedicalRecord> findByStatus(String status);

    /**
     * 添加病历
     */
    int insert(MedicalRecord medicalRecord);

    /**
     * 更新病历
     */
    int update(MedicalRecord medicalRecord);

    /**
     * 更新病历状态
     */
    int updateStatus(Integer recordId, String status);
} 