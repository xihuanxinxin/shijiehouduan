package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Surgery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 手术DAO接口
 */
public interface SurgeryDao {
    /**
     * 根据ID查询手术记录
     */
    Surgery findById(Integer surgeryId);

    /**
     * 根据病历ID查询手术记录
     */
    List<Surgery> findByRecordId(Integer recordId);

    /**
     * 根据患者ID查询手术记录
     */
    List<Surgery> findByPatientId(Integer patientId);

    /**
     * 根据医生ID查询手术记录
     */
    List<Surgery> findByDoctorId(Integer doctorId);

    /**
     * 根据状态查询手术记录
     */
    List<Surgery> findByStatus(String status);

    /**
     * 根据手术类型查询手术记录
     */
    List<Surgery> findByType(String surgeryType);

    /**
     * 查询所有手术记录
     */
    List<Surgery> findAll();

    /**
     * 新增手术记录
     */
    int insert(Surgery surgery);

    /**
     * 更新手术记录
     */
    int update(Surgery surgery);

    /**
     * 更新手术状态
     */
    int updateStatus(@Param("surgeryId") Integer surgeryId, @Param("status") String status);

    /**
     * 统计指定状态的手术记录数量
     */
    long countByStatus(String status);

    /**
     * 按手术类型统计手术记录数量
     */
    List<Map<String, Object>> countByType();
} 