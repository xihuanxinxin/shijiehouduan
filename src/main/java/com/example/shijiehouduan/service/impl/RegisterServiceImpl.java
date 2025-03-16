package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.dao.UserDao;
import com.example.shijiehouduan.dto.RegisterRequest;
import com.example.shijiehouduan.entity.Patient;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.PatientService;
import com.example.shijiehouduan.service.RegisterService;
import com.example.shijiehouduan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 注册服务实现类
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Result registerPatient(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        User existUser = userService.getUserByUsername(registerRequest.getUsername());
        if (existUser != null) {
            return Result.validateFailed("用户名已存在");
        }

        // 检查身份证号是否已存在（如果提供了身份证号）
        if (registerRequest.getIdCard() != null && !registerRequest.getIdCard().trim().isEmpty()) {
            Patient existPatient = patientService.getPatientByIdCard(registerRequest.getIdCard());
            if (existPatient != null) {
                return Result.validateFailed("身份证号已存在");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setName(registerRequest.getName());
        user.setGender(registerRequest.getGender());
        user.setPhone(registerRequest.getPhone());
        user.setRoleType("患者"); // 设置为患者角色
        user.setStatus("启用"); // 默认启用

        // 保存用户
        boolean userSaved = userService.addUser(user);
        if (!userSaved) {
            return Result.error("注册失败，请稍后重试");
        }

        // 创建患者信息
        Patient patient = new Patient();
        patient.setUserId(user.getUserId());
        patient.setIdCard(registerRequest.getIdCard());
        
        // 处理出生日期
        if (registerRequest.getBirthDate() != null && !registerRequest.getBirthDate().trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date birthDate = sdf.parse(registerRequest.getBirthDate());
                patient.setBirthDate(birthDate);
            } catch (ParseException e) {
                // 日期格式错误，不设置出生日期
            }
        }
        
        patient.setAddress(registerRequest.getAddress());
        patient.setEmergencyContact(registerRequest.getEmergencyContact());
        patient.setEmergencyPhone(registerRequest.getEmergencyPhone());
        patient.setMedicalHistory(registerRequest.getMedicalHistory());
        patient.setAllergyHistory(registerRequest.getAllergyHistory());

        // 保存患者信息
        boolean patientSaved = patientService.addPatient(patient);
        if (!patientSaved) {
            // 如果患者信息保存失败，回滚事务
            throw new RuntimeException("患者信息保存失败");
        }

        return Result.success("注册成功");
    }
} 