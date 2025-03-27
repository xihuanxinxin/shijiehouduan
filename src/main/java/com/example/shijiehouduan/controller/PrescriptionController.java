package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.Medicine;
import com.example.shijiehouduan.entity.Prescription;
import com.example.shijiehouduan.entity.PrescriptionMedicine;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.MedicineService;
import com.example.shijiehouduan.service.PatientService;
import com.example.shijiehouduan.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处方控制器
 */
@RestController
@RequestMapping("/api/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    /**
     * 添加处方
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPrescription(@RequestBody Map<String, Object> requestData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查用户是否登录
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("status", "fail");
            response.put("message", "请先登录");
            return ResponseEntity.status(401).body(response);
        }
        
        // 检查是否为医生用户
        if (!"医生".equals(user.getRoleType())) {
            response.put("status", "fail");
            response.put("message", "只有医生用户才能开具处方");
            return ResponseEntity.status(403).body(response);
        }
        
        // 获取医生ID
        Integer doctorId = doctorService.getDoctorByUserId(user.getUserId()).getDoctorId();
        
        try {
            // 解析处方基本信息
            Map<String, Object> prescriptionData = (Map<String, Object>) requestData.get("prescription");
            Prescription prescription = new Prescription();
            
            prescription.setRecordId((Integer) prescriptionData.get("recordId"));
            prescription.setPatientId((Integer) prescriptionData.get("patientId"));
            prescription.setDoctorId(doctorId);
            prescription.setIssueDate(new Date());
            prescription.setDiagnosis((String) prescriptionData.get("diagnosis"));
            prescription.setStatus("待发药");
            prescription.setRemarks((String) prescriptionData.get("remarks"));
            
            // 解析处方药品列表
            List<Map<String, Object>> medicationsData = (List<Map<String, Object>>) requestData.get("medications");
            List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<>();
            
            for (Map<String, Object> medicationData : medicationsData) {
                PrescriptionMedicine pm = new PrescriptionMedicine();
                pm.setMedicineId((Integer) medicationData.get("medicineId"));
                pm.setQuantity((Integer) medicationData.get("quantity"));
                pm.setUsageMethod((String) medicationData.get("dosage"));
                
                prescriptionMedicines.add(pm);
            }
            
            // 保存处方
            Integer prescriptionId = prescriptionService.addPrescription(prescription, prescriptionMedicines);
            
            response.put("status", "success");
            response.put("message", "处方开具成功");
            response.put("data", prescriptionId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "处方开具失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取处方列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getPrescriptionList(
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer recordId,
            @RequestParam(required = false) String status,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 检查用户是否登录
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("status", "fail");
            response.put("message", "请先登录");
            return ResponseEntity.status(401).body(response);
        }
        
        List<Prescription> prescriptions;
        
        // 根据用户角色和查询参数获取处方列表
        if ("患者".equals(user.getRoleType())) {
            // 患者只能查看自己的处方
            Integer userPatientId = patientService.getPatientByUserId(user.getUserId()).getPatientId();
            prescriptions = prescriptionService.getPrescriptionsByPatientId(userPatientId);
            
        } else if ("医生".equals(user.getRoleType())) {
            // 医生可以查看自己开具的处方，或指定患者的处方
            Integer userDoctorId = doctorService.getDoctorByUserId(user.getUserId()).getDoctorId();
            
            if (patientId != null) {
                prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
                // 过滤出该医生开具的处方
                prescriptions.removeIf(p -> !p.getDoctorId().equals(userDoctorId));
            } else {
                prescriptions = prescriptionService.getPrescriptionsByDoctorId(userDoctorId);
            }
            
        } else if ("管理员".equals(user.getRoleType())) {
            // 管理员可以查看所有处方，或按条件筛选
            if (patientId != null) {
                prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
            } else if (doctorId != null) {
                prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
            } else if (recordId != null) {
                prescriptions = prescriptionService.getPrescriptionsByRecordId(recordId);
            } else if (status != null) {
                prescriptions = prescriptionService.getPrescriptionsByStatus(status);
            } else {
                prescriptions = prescriptionService.getAllPrescriptions();
            }
        } else {
            response.put("status", "fail");
            response.put("message", "无效的用户角色");
            return ResponseEntity.status(400).body(response);
        }
        
        // 如果指定了status参数，进一步过滤
        if (status != null && !"管理员".equals(user.getRoleType())) {
            prescriptions.removeIf(p -> !p.getStatus().equals(status));
        }
        
        response.put("status", "success");
        response.put("data", prescriptions);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取处方详情
     */
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionDetail(@PathVariable Integer prescriptionId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查用户是否登录
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("status", "fail");
            response.put("message", "请先登录");
            return ResponseEntity.status(401).body(response);
        }
        
        // 获取处方详情
        Map<String, Object> prescriptionDetail = prescriptionService.getPrescriptionDetail(prescriptionId);
        if (prescriptionDetail == null) {
            response.put("status", "fail");
            response.put("message", "处方不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        Prescription prescription = (Prescription) prescriptionDetail.get("prescription");
        
        // 检查权限
        if ("患者".equals(user.getRoleType())) {
            // 患者只能查看自己的处方
            Integer userPatientId = patientService.getPatientByUserId(user.getUserId()).getPatientId();
            if (!prescription.getPatientId().equals(userPatientId)) {
                response.put("status", "fail");
                response.put("message", "无权查看此处方");
                return ResponseEntity.status(403).body(response);
            }
        } else if ("医生".equals(user.getRoleType())) {
            // 医生只能查看自己开具的处方
            Integer userDoctorId = doctorService.getDoctorByUserId(user.getUserId()).getDoctorId();
            if (!prescription.getDoctorId().equals(userDoctorId)) {
                response.put("status", "fail");
                response.put("message", "无权查看此处方");
                return ResponseEntity.status(403).body(response);
            }
        }
        
        response.put("status", "success");
        response.put("data", prescriptionDetail);
        return ResponseEntity.ok(response);
    }

    /**
     * 更新处方状态
     */
    @PutMapping("/{prescriptionId}/status")
    public ResponseEntity<Map<String, Object>> updatePrescriptionStatus(@PathVariable Integer prescriptionId, @RequestBody String status, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查用户是否登录
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("status", "fail");
            response.put("message", "请先登录");
            return ResponseEntity.status(401).body(response);
        }
        
        // 只有医生和管理员可以更新处方状态
        if (!"医生".equals(user.getRoleType()) && !"管理员".equals(user.getRoleType())) {
            response.put("status", "fail");
            response.put("message", "无权更新处方状态");
            return ResponseEntity.status(403).body(response);
        }
        
        // 验证状态值
        if (!"待发药".equals(status) && !"已发药".equals(status) && !"已取消".equals(status)) {
            response.put("status", "fail");
            response.put("message", "无效的处方状态");
            return ResponseEntity.status(400).body(response);
        }
        
        // 如果是医生，需要验证是该医生开具的处方
        if ("医生".equals(user.getRoleType())) {
            Prescription prescription = prescriptionService.getPrescriptionById(prescriptionId);
            if (prescription == null) {
                response.put("status", "fail");
                response.put("message", "处方不存在");
                return ResponseEntity.status(404).body(response);
            }
            
            Integer userDoctorId = doctorService.getDoctorByUserId(user.getUserId()).getDoctorId();
            if (!prescription.getDoctorId().equals(userDoctorId)) {
                response.put("status", "fail");
                response.put("message", "无权更新此处方状态");
                return ResponseEntity.status(403).body(response);
            }
        }
        
        // 更新处方状态
        boolean result = prescriptionService.updatePrescriptionStatus(prescriptionId, status);
        if (result) {
            response.put("status", "success");
            response.put("message", "处方状态更新成功");
        } else {
            response.put("status", "fail");
            response.put("message", "处方状态更新失败");
            return ResponseEntity.status(500).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取药品列表（供医生开处方使用）
     */
    @GetMapping("/medicines")
    public ResponseEntity<Map<String, Object>> getMedicineList(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查用户是否登录
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("status", "fail");
            response.put("message", "请先登录");
            return ResponseEntity.status(401).body(response);
        }
        
        // 获取药品列表
        List<Medicine> medicines = medicineService.getAllMedicines();
        
        response.put("status", "success");
        response.put("data", medicines);
        return ResponseEntity.ok(response);
    }
} 