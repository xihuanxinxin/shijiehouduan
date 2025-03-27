package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Prescription;
import com.example.shijiehouduan.entity.PrescriptionMedicine;

import java.util.List;
import java.util.Map;

/**
 * 处方服务接口
 */
public interface PrescriptionService {
    /**
     * 根据ID查询处方
     */
    Prescription getPrescriptionById(Integer prescriptionId);

    /**
     * 根据患者ID查询处方列表
     */
    List<Prescription> getPrescriptionsByPatientId(Integer patientId);

    /**
     * 根据医生ID查询处方列表
     */
    List<Prescription> getPrescriptionsByDoctorId(Integer doctorId);

    /**
     * 根据病历ID查询处方列表
     */
    List<Prescription> getPrescriptionsByRecordId(Integer recordId);
    
    /**
     * 根据状态查询处方列表
     */
    List<Prescription> getPrescriptionsByStatus(String status);
    
    /**
     * 获取所有处方
     */
    List<Prescription> getAllPrescriptions();
    
    /**
     * 添加处方（包含处方药品）
     * @param prescription 处方信息
     * @param prescriptionMedicines 处方药品列表
     * @return 处方ID
     */
    Integer addPrescription(Prescription prescription, List<PrescriptionMedicine> prescriptionMedicines);
    
    /**
     * 更新处方
     */
    boolean updatePrescription(Prescription prescription);
    
    /**
     * 更新处方状态
     */
    boolean updatePrescriptionStatus(Integer prescriptionId, String status);
    
    /**
     * 获取处方详细信息（包含处方药品详情）
     */
    Map<String, Object> getPrescriptionDetail(Integer prescriptionId);
} 