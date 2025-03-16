package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Appointment;
import java.util.Date;
import java.util.List;

/**
 * 预约挂号服务接口
 */
public interface AppointmentService {
    /**
     * 根据ID查询预约
     */
    Appointment getAppointmentById(Integer appointmentId);

    /**
     * 根据患者ID查询预约列表
     */
    List<Appointment> getAppointmentsByPatientId(Integer patientId);

    /**
     * 根据医生ID查询预约列表
     */
    List<Appointment> getAppointmentsByDoctorId(Integer doctorId);

    /**
     * 根据医生ID和日期查询预约列表
     */
    List<Appointment> getAppointmentsByDoctorIdAndDate(Integer doctorId, Date appointmentDate);

    /**
     * 根据状态查询预约列表
     */
    List<Appointment> getAppointmentsByStatus(String status);

    /**
     * 添加预约
     */
    boolean addAppointment(Appointment appointment);

    /**
     * 更新预约
     */
    boolean updateAppointment(Appointment appointment);

    /**
     * 取消预约
     */
    boolean cancelAppointment(Integer appointmentId);

    /**
     * 确认预约
     */
    boolean confirmAppointment(Integer appointmentId);

    /**
     * 完成预约
     */
    boolean completeAppointment(Integer appointmentId);
} 