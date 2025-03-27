package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Hospitalization;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 住院记录DAO接口
 */
public interface HospitalizationDao {
    /**
     * 根据ID查询住院记录
     */
    Hospitalization findById(Integer hospitalizationId);

    /**
     * 根据患者ID查询住院记录列表
     */
    List<Hospitalization> findByPatientId(Integer patientId);

    /**
     * 根据医生ID查询住院记录列表
     */
    List<Hospitalization> findByDoctorId(Integer doctorId);

    /**
     * 根据床位ID查询住院记录
     */
    Hospitalization findByBedId(Integer bedId);

    /**
     * 查询当前在院的住院记录
     */
    List<Hospitalization> findCurrentHospitalizations();
    
    /**
     * 添加住院记录
     */
    int insert(Hospitalization hospitalization);
    
    /**
     * 更新住院记录
     */
    int update(Hospitalization hospitalization);
    
    /**
     * 更新住院记录状态
     */
    int updateStatus(@Param("hospitalizationId") Integer hospitalizationId, @Param("status") String status);
    
    /**
     * 更新出院日期
     */
    int updateDischargeDate(@Param("hospitalizationId") Integer hospitalizationId, @Param("dischargeDate") java.util.Date dischargeDate);
    
    /**
     * 根据状态查询住院记录列表
     */
    List<Hospitalization> findByStatus(String status);
} 