package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Doctor;
import java.util.List;

/**
 * 医生服务接口
 */
public interface DoctorService {
    /**
     * 根据ID查询医生
     */
    Doctor getDoctorById(Integer doctorId);

    /**
     * 根据用户ID查询医生
     */
    Doctor getDoctorByUserId(Integer userId);
    
    /**
     * 查询所有医生
     */
    List<Doctor> getAllDoctors();
    
    /**
     * 根据专业查询医生
     */
    List<Doctor> getDoctorsBySpecialty(String specialty);
    
    /**
     * 根据职称查询医生
     */
    List<Doctor> getDoctorsByTitle(String title);
    
    /**
     * 新增医生
     */
    boolean addDoctor(Doctor doctor);
    
    /**
     * 更新医生
     */
    boolean updateDoctor(Doctor doctor);
    
    /**
     * 更新医生排班
     */
    boolean updateDoctorSchedule(Integer doctorId, String schedule);
    
    /**
     * 删除医生
     */
    boolean deleteDoctor(Integer doctorId);
} 