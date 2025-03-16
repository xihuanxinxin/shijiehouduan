package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.MedicalRecord;
import java.util.List;

/**
 * 病历服务接口
 */
public interface MedicalRecordService {
    /**
     * 根据ID查询病历
     */
    MedicalRecord getMedicalRecordById(Integer recordId);

    /**
     * 根据患者ID查询病历列表
     */
    List<MedicalRecord> getMedicalRecordsByPatientId(Integer patientId);

    /**
     * 根据医生ID查询病历列表
     */
    List<MedicalRecord> getMedicalRecordsByDoctorId(Integer doctorId);

    /**
     * 根据状态查询病历列表
     */
    List<MedicalRecord> getMedicalRecordsByStatus(String status);

    /**
     * 添加病历
     */
    boolean addMedicalRecord(MedicalRecord medicalRecord);

    /**
     * 更新病历
     */
    boolean updateMedicalRecord(MedicalRecord medicalRecord);

    /**
     * 更新病历状态为待诊
     */
    boolean setMedicalRecordPending(Integer recordId);

    /**
     * 更新病历状态为诊疗中
     */
    boolean setMedicalRecordInProgress(Integer recordId);

    /**
     * 更新病历状态为已完成
     */
    boolean setMedicalRecordCompleted(Integer recordId);

    /**
     * 更新病历状态为已取消
     */
    boolean setMedicalRecordCancelled(Integer recordId);
} 