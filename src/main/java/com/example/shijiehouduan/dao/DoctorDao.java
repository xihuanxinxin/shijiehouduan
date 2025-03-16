package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Doctor;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 医生DAO接口
 */
public interface DoctorDao {
    /**
     * 根据ID查询医生
     */
    Doctor findById(Integer doctorId);

    /**
     * 根据用户ID查询医生
     */
    Doctor findByUserId(Integer userId);
    
    /**
     * 查询所有医生
     */
    List<Doctor> findAll();
    
    /**
     * 根据专业查询医生
     */
    List<Doctor> findBySpecialty(String specialty);
    
    /**
     * 根据职称查询医生
     */
    List<Doctor> findByTitle(String title);
    
    /**
     * 新增医生
     */
    int insert(Doctor doctor);
    
    /**
     * 更新医生
     */
    int update(Doctor doctor);
    
    /**
     * 更新医生排班
     */
    int updateSchedule(@Param("doctorId") Integer doctorId, @Param("schedule") String schedule);
    
    /**
     * 删除医生
     */
    int delete(Integer doctorId);
} 