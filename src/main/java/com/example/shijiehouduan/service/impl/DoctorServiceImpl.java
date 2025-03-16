package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.DoctorDao;
import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 医生服务实现类
 */
@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorDao doctorDao;

    @Override
    public Doctor getDoctorById(Integer doctorId) {
        return doctorDao.findById(doctorId);
    }

    @Override
    public Doctor getDoctorByUserId(Integer userId) {
        return doctorDao.findByUserId(userId);
    }
    
    @Override
    public List<Doctor> getAllDoctors() {
        return doctorDao.findAll();
    }
    
    @Override
    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        return doctorDao.findBySpecialty(specialty);
    }
    
    @Override
    public List<Doctor> getDoctorsByTitle(String title) {
        return doctorDao.findByTitle(title);
    }
    
    @Override
    @Transactional
    public boolean addDoctor(Doctor doctor) {
        // 设置创建时间
        if (doctor.getCreatedAt() == null) {
            doctor.setCreatedAt(new Date());
        }
        
        return doctorDao.insert(doctor) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateDoctor(Doctor doctor) {
        // 检查医生是否存在
        Doctor existingDoctor = doctorDao.findById(doctor.getDoctorId());
        if (existingDoctor == null) {
            return false;
        }
        
        return doctorDao.update(doctor) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateDoctorSchedule(Integer doctorId, String schedule) {
        return doctorDao.updateSchedule(doctorId, schedule) > 0;
    }

    @Override
    @Transactional
    public boolean deleteDoctor(Integer doctorId) {
        // 检查医生是否存在
        Doctor existingDoctor = doctorDao.findById(doctorId);
        if (existingDoctor == null) {
            return false;
        }
        
        return doctorDao.delete(doctorId) > 0;
    }
} 