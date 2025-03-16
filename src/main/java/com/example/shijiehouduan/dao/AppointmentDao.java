package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Appointment;
import java.util.Date;
import java.util.List;

/**
 * 预约挂号DAO接口
 */
public interface AppointmentDao {
    /**
     * 根据ID查询预约
     */
    Appointment findById(Integer appointmentId);

    /**
     * 根据患者ID查询预约列表
     */
    List<Appointment> findByPatientId(Integer patientId);

    /**
     * 根据医生ID查询预约列表
     */
    List<Appointment> findByDoctorId(Integer doctorId);

    /**
     * 根据医生ID和日期查询预约列表
     */
    List<Appointment> findByDoctorIdAndDate(Integer doctorId, Date appointmentDate);

    /**
     * 根据状态查询预约列表
     */
    List<Appointment> findByStatus(String status);

    /**
     * 添加预约
     */
    int insert(Appointment appointment);

    /**
     * 更新预约
     */
    int update(Appointment appointment);

    /**
     * 删除预约
     */
    int deleteById(Integer appointmentId);
} 