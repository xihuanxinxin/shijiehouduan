package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.EyeExamination;
import java.util.List;
import java.util.Map;

/**
 * 眼科检查记录DAO接口
 */
public interface EyeExaminationDao {
    /**
     * 根据ID查询眼科检查记录
     */
    EyeExamination findById(Integer examinationId);

    /**
     * 根据病历ID查询眼科检查记录列表
     */
    List<EyeExamination> findByRecordId(Integer recordId);

    /**
     * 根据患者ID查询眼科检查记录列表
     */
    List<EyeExamination> findByPatientId(Integer patientId);

    /**
     * 根据医生ID查询眼科检查记录列表
     */
    List<EyeExamination> findByDoctorId(Integer doctorId);
    
    /**
     * 查询所有眼科检查记录
     */
    List<EyeExamination> findAll();
    
    /**
     * 添加眼科检查记录
     */
    int insert(EyeExamination eyeExamination);
    
    /**
     * 更新眼科检查记录
     */
    int update(EyeExamination eyeExamination);

    /**
     * 统计所有眼科检查记录数量
     */
    long countAll();

    /**
     * 按医生统计眼科检查记录数量
     */
    List<Map<String, Object>> countByDoctor();
} 