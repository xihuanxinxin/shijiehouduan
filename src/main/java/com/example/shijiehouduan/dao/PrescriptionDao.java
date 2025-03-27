package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Prescription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 处方DAO接口
 */
public interface PrescriptionDao {
    /**
     * 根据ID查询处方
     */
    Prescription findById(Integer prescriptionId);

    /**
     * 根据患者ID查询处方列表
     */
    List<Prescription> findByPatientId(Integer patientId);

    /**
     * 根据医生ID查询处方列表
     */
    List<Prescription> findByDoctorId(Integer doctorId);

    /**
     * 根据病历ID查询处方列表
     */
    List<Prescription> findByRecordId(Integer recordId);
    
    /**
     * 根据状态查询处方列表
     */
    List<Prescription> findByStatus(String status);
    
    /**
     * 添加处方
     */
    int insert(Prescription prescription);
    
    /**
     * 更新处方
     */
    int update(Prescription prescription);
    
    /**
     * 更新处方状态
     */
    int updateStatus(@Param("prescriptionId") Integer prescriptionId, @Param("status") String status);
    
    /**
     * 获取所有处方列表
     */
    List<Prescription> findAll();
} 