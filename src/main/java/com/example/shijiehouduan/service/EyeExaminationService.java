package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.EyeExamination;
import java.util.List;
import java.util.Map;

/**
 * 眼科检查记录服务接口
 */
public interface EyeExaminationService {
    /**
     * 根据ID查询眼科检查记录
     */
    EyeExamination getEyeExaminationById(Integer examinationId);

    /**
     * 根据病历ID查询眼科检查记录列表
     */
    List<EyeExamination> getEyeExaminationsByRecordId(Integer recordId);

    /**
     * 根据患者ID查询眼科检查记录列表
     */
    List<EyeExamination> getEyeExaminationsByPatientId(Integer patientId);

    /**
     * 根据医生ID查询眼科检查记录列表
     */
    List<EyeExamination> getEyeExaminationsByDoctorId(Integer doctorId);
    
    /**
     * 查询所有眼科检查记录
     */
    List<EyeExamination> getAllEyeExaminations();
    
    /**
     * 添加眼科检查记录
     */
    boolean addEyeExamination(EyeExamination eyeExamination);
    
    /**
     * 更新眼科检查记录
     */
    boolean updateEyeExamination(EyeExamination eyeExamination);

    /**
     * 统计所有眼科检查记录数量
     */
    long countAllEyeExaminations();

    /**
     * 按医生统计眼科检查记录数量
     */
    List<Map<String, Object>> countEyeExaminationsByDoctor();
} 