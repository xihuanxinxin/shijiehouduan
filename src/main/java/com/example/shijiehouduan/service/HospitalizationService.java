package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Hospitalization;
import java.util.List;
import java.util.Date;

/**
 * 住院记录服务接口
 */
public interface HospitalizationService {
    /**
     * 根据ID查询住院记录
     */
    Hospitalization getHospitalizationById(Integer hospitalizationId);

    /**
     * 根据患者ID查询住院记录列表
     */
    List<Hospitalization> getHospitalizationsByPatientId(Integer patientId);

    /**
     * 根据医生ID查询住院记录列表
     */
    List<Hospitalization> getHospitalizationsByDoctorId(Integer doctorId);

    /**
     * 根据床位ID查询住院记录
     */
    Hospitalization getHospitalizationByBedId(Integer bedId);

    /**
     * 查询当前在院的住院记录
     */
    List<Hospitalization> getCurrentHospitalizations();
    
    /**
     * 添加住院记录
     */
    boolean addHospitalization(Hospitalization hospitalization);
    
    /**
     * 更新住院记录
     */
    boolean updateHospitalization(Hospitalization hospitalization);
    
    /**
     * 更新住院记录状态为在院
     */
    boolean setHospitalizationInHospital(Integer hospitalizationId);
    
    /**
     * 更新住院记录状态为出院
     */
    boolean setHospitalizationDischarged(Integer hospitalizationId, Date dischargeDate);
    
    /**
     * 更新住院记录状态为转院
     */
    boolean setHospitalizationTransferred(Integer hospitalizationId, Date dischargeDate);
    
    /**
     * 更新住院记录状态为死亡
     */
    boolean setHospitalizationDeceased(Integer hospitalizationId, Date dischargeDate);
    
    /**
     * 根据状态查询住院记录列表
     */
    List<Hospitalization> getHospitalizationsByStatus(String status);
} 