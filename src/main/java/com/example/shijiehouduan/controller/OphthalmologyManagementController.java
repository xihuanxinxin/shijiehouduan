package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.EyeExamination;
import com.example.shijiehouduan.entity.Surgery;
import com.example.shijiehouduan.service.EyeExaminationService;
import com.example.shijiehouduan.service.SurgeryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 眼科专项管理控制器
 */
@RestController
@RequestMapping("/api/admin/ophthalmology")
public class OphthalmologyManagementController {

    @Autowired
    private EyeExaminationService eyeExaminationService;
    
    @Autowired
    private SurgeryService surgeryService;

    /**
     * 获取所有眼科检查记录
     */
    @GetMapping("/examinations")
    public ResponseEntity<Map<String, Object>> getAllEyeExaminations() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<EyeExamination> examinations = eyeExaminationService.getAllEyeExaminations();
            response.put("success", true);
            response.put("message", "获取眼科检查记录列表成功");
            response.put("data", examinations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取眼科检查记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据ID获取眼科检查记录
     */
    @GetMapping("/examinations/{examinationId}")
    public ResponseEntity<Map<String, Object>> getEyeExaminationById(@PathVariable Integer examinationId) {
        Map<String, Object> response = new HashMap<>();
        try {
            EyeExamination examination = eyeExaminationService.getEyeExaminationById(examinationId);
            if (examination != null) {
                response.put("success", true);
                response.put("message", "获取眼科检查记录成功");
                response.put("data", examination);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "眼科检查记录不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取眼科检查记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据病历ID获取眼科检查记录
     */
    @GetMapping("/examinations/record/{recordId}")
    public ResponseEntity<Map<String, Object>> getEyeExaminationsByRecordId(@PathVariable Integer recordId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<EyeExamination> examinations = eyeExaminationService.getEyeExaminationsByRecordId(recordId);
            response.put("success", true);
            response.put("message", "获取眼科检查记录列表成功");
            response.put("data", examinations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取眼科检查记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据患者ID获取眼科检查记录
     */
    @GetMapping("/examinations/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getEyeExaminationsByPatientId(@PathVariable Integer patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<EyeExamination> examinations = eyeExaminationService.getEyeExaminationsByPatientId(patientId);
            response.put("success", true);
            response.put("message", "获取眼科检查记录列表成功");
            response.put("data", examinations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取眼科检查记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据医生ID获取眼科检查记录
     */
    @GetMapping("/examinations/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> getEyeExaminationsByDoctorId(@PathVariable Integer doctorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<EyeExamination> examinations = eyeExaminationService.getEyeExaminationsByDoctorId(doctorId);
            response.put("success", true);
            response.put("message", "获取眼科检查记录列表成功");
            response.put("data", examinations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取眼科检查记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取所有手术记录
     */
    @GetMapping("/surgeries")
    public ResponseEntity<Map<String, Object>> getAllSurgeries() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Surgery> surgeries = surgeryService.getAllSurgeries();
            response.put("success", true);
            response.put("message", "获取手术记录列表成功");
            response.put("data", surgeries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取手术记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据ID获取手术记录
     */
    @GetMapping("/surgeries/{surgeryId}")
    public ResponseEntity<Map<String, Object>> getSurgeryById(@PathVariable Integer surgeryId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Surgery surgery = surgeryService.getSurgeryById(surgeryId);
            if (surgery != null) {
                response.put("success", true);
                response.put("message", "获取手术记录成功");
                response.put("data", surgery);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "手术记录不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取手术记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据病历ID获取手术记录
     */
    @GetMapping("/surgeries/record/{recordId}")
    public ResponseEntity<Map<String, Object>> getSurgeriesByRecordId(@PathVariable Integer recordId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByRecordId(recordId);
            response.put("success", true);
            response.put("message", "获取手术记录列表成功");
            response.put("data", surgeries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取手术记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据患者ID获取手术记录
     */
    @GetMapping("/surgeries/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getSurgeriesByPatientId(@PathVariable Integer patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByPatientId(patientId);
            response.put("success", true);
            response.put("message", "获取手术记录列表成功");
            response.put("data", surgeries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取手术记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据医生ID获取手术记录
     */
    @GetMapping("/surgeries/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> getSurgeriesByDoctorId(@PathVariable Integer doctorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByDoctorId(doctorId);
            response.put("success", true);
            response.put("message", "获取手术记录列表成功");
            response.put("data", surgeries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取手术记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据状态获取手术记录
     */
    @GetMapping("/surgeries/status/{status}")
    public ResponseEntity<Map<String, Object>> getSurgeriesByStatus(@PathVariable String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByStatus(status);
            response.put("success", true);
            response.put("message", "获取手术记录列表成功");
            response.put("data", surgeries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取手术记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据手术类型获取手术记录
     */
    @GetMapping("/surgeries/type")
    public ResponseEntity<Map<String, Object>> getSurgeriesByType(@RequestParam String surgeryType) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Surgery> surgeries = surgeryService.getSurgeriesByType(surgeryType);
            response.put("success", true);
            response.put("message", "获取手术记录列表成功");
            response.put("data", surgeries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取手术记录列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取手术统计数据
     */
    @GetMapping("/surgeries/statistics")
    public ResponseEntity<Map<String, Object>> getSurgeryStatistics() {
        Map<String, Object> response = new HashMap<>();
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
            
            response.put("success", true);
            response.put("message", "获取手术统计数据成功");
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取手术统计数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取眼科检查统计数据
     */
    @GetMapping("/examinations/statistics")
    public ResponseEntity<Map<String, Object>> getEyeExaminationStatistics() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总检查数量
            long totalCount = eyeExaminationService.countAllEyeExaminations();
            statistics.put("totalCount", totalCount);
            
            // 按医生统计
            List<Map<String, Object>> doctorStats = eyeExaminationService.countEyeExaminationsByDoctor();
            statistics.put("doctorStats", doctorStats);
            
            response.put("success", true);
            response.put("message", "获取眼科检查统计数据成功");
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取眼科检查统计数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 