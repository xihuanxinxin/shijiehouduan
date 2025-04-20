package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.EyeExamination;
import com.example.shijiehouduan.entity.Surgery;
import com.example.shijiehouduan.service.EyeExaminationService;
import com.example.shijiehouduan.service.SurgeryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 眼科专项管理控制器
 */
@RestController
@RequestMapping("/api/admin/ophthalmology")
public class OphthalmologyManagementController extends BaseController {

    @Autowired
    private EyeExaminationService eyeExaminationService;
    
    @Autowired
    private SurgeryService surgeryService;

    /**
     * 获取所有眼科检查记录
     */
    @GetMapping("/examinations")
    public Result getAllEyeExaminations(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<EyeExamination> examinations = eyeExaminationService.getAllEyeExaminations();
            return Result.success(examinations);
        } catch (Exception e) {
            return Result.error("获取眼科检查记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取眼科检查记录
     */
    @GetMapping("/examinations/{examinationId}")
    public Result getEyeExaminationById(@PathVariable Integer examinationId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            EyeExamination examination = eyeExaminationService.getEyeExaminationById(examinationId);
            if (examination != null) {
                return Result.success(examination);
            } else {
                return Result.error("眼科检查记录不存在");
            }
        } catch (Exception e) {
            return Result.error("获取眼科检查记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据病历ID获取眼科检查记录
     */
    @GetMapping("/examinations/record/{recordId}")
    public Result getEyeExaminationsByRecordId(@PathVariable Integer recordId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<EyeExamination> examinations = eyeExaminationService.getEyeExaminationsByRecordId(recordId);
            return Result.success(examinations);
        } catch (Exception e) {
            return Result.error("获取眼科检查记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID获取眼科检查记录
     */
    @GetMapping("/examinations/patient/{patientId}")
    public Result getEyeExaminationsByPatientId(@PathVariable Integer patientId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<EyeExamination> examinations = eyeExaminationService.getEyeExaminationsByPatientId(patientId);
            return Result.success(examinations);
        } catch (Exception e) {
            return Result.error("获取眼科检查记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据医生ID获取眼科检查记录
     */
    @GetMapping("/examinations/doctor/{doctorId}")
    public Result getEyeExaminationsByDoctorId(@PathVariable Integer doctorId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<EyeExamination> examinations = eyeExaminationService.getEyeExaminationsByDoctorId(doctorId);
            return Result.success(examinations);
        } catch (Exception e) {
            return Result.error("获取眼科检查记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有手术记录
     */
    @GetMapping("/surgeries")
    public Result getAllSurgeries(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Surgery> surgeries = surgeryService.getAllSurgeries();
            return Result.success(surgeries);
        } catch (Exception e) {
            return Result.error("获取手术记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取手术记录
     */
    @GetMapping("/surgeries/{surgeryId}")
    public Result getSurgeryById(@PathVariable Integer surgeryId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            Surgery surgery = surgeryService.getSurgeryById(surgeryId);
            if (surgery != null) {
                return Result.success(surgery);
            } else {
                return Result.error("手术记录不存在");
            }
        } catch (Exception e) {
            return Result.error("获取手术记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据病历ID获取手术记录
     */
    @GetMapping("/surgeries/record/{recordId}")
    public Result getSurgeriesByRecordId(@PathVariable Integer recordId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByRecordId(recordId);
            return Result.success(surgeries);
        } catch (Exception e) {
            return Result.error("获取手术记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID获取手术记录
     */
    @GetMapping("/surgeries/patient/{patientId}")
    public Result getSurgeriesByPatientId(@PathVariable Integer patientId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByPatientId(patientId);
            return Result.success(surgeries);
        } catch (Exception e) {
            return Result.error("获取手术记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据医生ID获取手术记录
     */
    @GetMapping("/surgeries/doctor/{doctorId}")
    public Result getSurgeriesByDoctorId(@PathVariable Integer doctorId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByDoctorId(doctorId);
            return Result.success(surgeries);
        } catch (Exception e) {
            return Result.error("获取手术记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态获取手术记录
     */
    @GetMapping("/surgeries/status/{status}")
    public Result getSurgeriesByStatus(@PathVariable String status, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByStatus(status);
            return Result.success(surgeries);
        } catch (Exception e) {
            return Result.error("获取手术记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据手术类型获取手术记录
     */
    @GetMapping("/surgeries/type")
    public Result getSurgeriesByType(@RequestParam String surgeryType, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByType(surgeryType);
            return Result.success(surgeries);
        } catch (Exception e) {
            return Result.error("获取手术记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取手术统计数据
     */
    @GetMapping("/surgeries/statistics")
    public Result getSurgeryStatistics(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 按状态统计
            Map<String, Long> statusStats = new HashMap<>();
            statusStats.put("待手术", surgeryService.countSurgeriesByStatus("待手术"));
            statusStats.put("手术中", surgeryService.countSurgeriesByStatus("手术中"));
            statusStats.put("已完成", surgeryService.countSurgeriesByStatus("已完成"));
            statusStats.put("已取消", surgeryService.countSurgeriesByStatus("已取消"));
            statistics.put("statusStats", statusStats);
            
            // 按类型统计
            List<Map<String, Object>> typeStats = surgeryService.countSurgeriesByType();
            statistics.put("typeStats", typeStats);
            
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取手术统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取眼科检查统计数据
     */
    @GetMapping("/examinations/statistics")
    public Result getEyeExaminationStatistics(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总检查数量
            long totalCount = eyeExaminationService.countAllEyeExaminations();
            statistics.put("totalCount", totalCount);
            
            // 按医生统计
            List<Map<String, Object>> doctorStats = eyeExaminationService.countEyeExaminationsByDoctor();
            statistics.put("doctorStats", doctorStats);
            
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取眼科检查统计数据失败: " + e.getMessage());
        }
    }
} 