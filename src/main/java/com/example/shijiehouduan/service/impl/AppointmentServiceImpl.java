package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.AppointmentDao;
import com.example.shijiehouduan.entity.Appointment;
import com.example.shijiehouduan.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 预约挂号服务实现类
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Override
    public Appointment getAppointmentById(Integer appointmentId) {
        return appointmentDao.findById(appointmentId);
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(Integer patientId) {
        return appointmentDao.findByPatientId(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(Integer doctorId) {
        return appointmentDao.findByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorIdAndDate(Integer doctorId, Date appointmentDate) {
        return appointmentDao.findByDoctorIdAndDate(doctorId, appointmentDate);
    }

    @Override
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentDao.findByStatus(status);
    }

    @Override
    @Transactional
    public boolean addAppointment(Appointment appointment) {
        // 设置默认状态为待确认
        if (appointment.getStatus() == null) {
            appointment.setStatus("待确认");
        }
        // 设置创建时间
        if (appointment.getCreatedAt() == null) {
            appointment.setCreatedAt(new Date());
        }
        return appointmentDao.insert(appointment) > 0;
    }

    @Override
    @Transactional
    public boolean updateAppointment(Appointment appointment) {
        return appointmentDao.update(appointment) > 0;
    }

    @Override
    @Transactional
    public boolean cancelAppointment(Integer appointmentId) {
        Appointment appointment = appointmentDao.findById(appointmentId);
        if (appointment == null) {
            return false;
        }
        appointment.setStatus("已取消");
        return appointmentDao.update(appointment) > 0;
    }

    @Override
    @Transactional
    public boolean confirmAppointment(Integer appointmentId) {
        Appointment appointment = appointmentDao.findById(appointmentId);
        if (appointment == null) {
            return false;
        }
        appointment.setStatus("已确认");
        return appointmentDao.update(appointment) > 0;
    }

    @Override
    @Transactional
    public boolean completeAppointment(Integer appointmentId) {
        Appointment appointment = appointmentDao.findById(appointmentId);
        if (appointment == null) {
            return false;
        }
        appointment.setStatus("已完成");
        return appointmentDao.update(appointment) > 0;
    }
} 