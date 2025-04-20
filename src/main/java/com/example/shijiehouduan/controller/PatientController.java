package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Patient;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 患者控制器
 */
@RestController
@RequestMapping("/api/patient")
public class PatientController extends BaseController {

    @Autowired
    private PatientService patientService;

    /**
     * 获取患者信息
     * @param patientId 患者ID（医生和管理员必填，患者不填则查询自己）
     * @param request HTTP请求
     * @return 患者信息
     */
    @GetMapping("/info")
    public Result getPatientInfo(@RequestParam(required = false) Integer patientId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色和参数判断查询哪个患者
        Patient patient = null;
        if (isPatient(request)) {
            // 患者只能查看自己的信息
            if (patientId != null && !isPatientSelf(currentUser.getUserId(), patientId)) {
                return Result.forbidden();
            }
            patient = patientService.getPatientByUserId(currentUser.getUserId());
        } else if (isDoctor(request) || isAdmin(request)) {
            // 医生和管理员可以查看指定患者的信息
            if (patientId == null) {
                return Result.validateFailed("患者ID不能为空");
            }
            patient = patientService.getPatientById(patientId);
        } else {
            return Result.forbidden();
        }

        if (patient == null) {
            return Result.error("患者不存在");
        }

        // 获取患者关联的用户信息
        User user = userService.getUserById(patient.getUserId());
        patient.setUser(user);

        return Result.success(patient);
    }

    /**
     * 更新患者信息
     * @param patient 患者信息
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/info")
    public Result updatePatientInfo(@RequestBody Patient patient, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色判断是否有权限更新
        if (isPatient(request)) {
            // 患者只能更新自己的信息
            Patient existingPatient = patientService.getPatientByUserId(currentUser.getUserId());
            if (existingPatient == null || !existingPatient.getPatientId().equals(patient.getPatientId())) {
                return Result.forbidden();
            }
        } else if (!isAdmin(request)) {
            // 只有患者和管理员可以更新患者信息
            return Result.forbidden();
        }

        // 更新患者信息
        boolean updated = patientService.updatePatient(patient);
        if (updated) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 判断是否是患者本人
     * @param userId 用户ID
     * @param patientId 患者ID
     * @return 是否是患者本人
     */
    private boolean isPatientSelf(Integer userId, Integer patientId) {
        Patient patient = patientService.getPatientById(patientId);
        return patient != null && patient.getUserId().equals(userId);
    }
} 