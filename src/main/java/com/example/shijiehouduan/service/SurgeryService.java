package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Surgery;
import java.util.List;
import java.util.Map;

/**
 * 手术服务接口
 */
public interface SurgeryService {
    /**
     * 根据ID查询手术记录
     */
    Surgery getSurgeryById(Integer surgeryId);

    /**
     * 根据病历ID查询手术记录
     */
    List<Surgery> getSurgeriesByRecordId(Integer recordId);

    /**
     * 根据患者ID查询手术记录
     */
    List<Surgery> getSurgeriesByPatientId(Integer patientId);

    /**
     * 根据医生ID查询手术记录
     */
    List<Surgery> getSurgeriesByDoctorId(Integer doctorId);

    /**
     * 根据状态查询手术记录
     */
    List<Surgery> getSurgeriesByStatus(String status);

    /**
     * 根据手术类型查询手术记录
     */
    List<Surgery> getSurgeriesByType(String surgeryType);

    /**
     * 查询所有手术记录
     */
    List<Surgery> getAllSurgeries();

    /**
     * 添加手术记录
     */
    boolean addSurgery(Surgery surgery);

    /**
     * 更新手术记录
     */
    boolean updateSurgery(Surgery surgery);

    /**
     * 更新手术状态
     */
    boolean updateSurgeryStatus(Integer surgeryId, String status);

    /**
     * 统计指定状态的手术记录数量
     */
    long countSurgeriesByStatus(String status);

    /**
     * 按手术类型统计手术记录数量
     */
    List<Map<String, Object>> countSurgeriesByType();
} 