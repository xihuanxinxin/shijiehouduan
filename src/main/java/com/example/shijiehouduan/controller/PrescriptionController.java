package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Medicine;
import com.example.shijiehouduan.entity.Prescription;
import com.example.shijiehouduan.entity.PrescriptionMedicine;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.MedicineService;
import com.example.shijiehouduan.service.PatientService;
import com.example.shijiehouduan.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
public class PrescriptionController extends BaseController {

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
    public Result addPrescription(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 检查是否为医生用户
        if (!isDoctor(request)) {
            return Result.forbidden();
        }
        
        // 获取医生ID
        Integer doctorId = doctorService.getDoctorByUserId(currentUser.getUserId()).getDoctorId();
        
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
            
            return Result.success("处方开具成功", prescriptionId);
            
        } catch (Exception e) {
            return Result.error("处方开具失败: " + e.getMessage());
        }
    }

    /**
     * 获取处方列表
     */
    @GetMapping("/list")
    public Result getPrescriptionList(
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer recordId,
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        List<Prescription> prescriptions;
        
        // 根据用户角色和查询参数获取处方列表
        if (isPatient(request)) {
            // 患者只能查看自己的处方
            Integer userPatientId = patientService.getPatientByUserId(currentUser.getUserId()).getPatientId();
            prescriptions = prescriptionService.getPrescriptionsByPatientId(userPatientId);
            
        } else if (isDoctor(request)) {
            // 医生可以查看自己开具的处方，或指定患者的处方
            Integer userDoctorId = doctorService.getDoctorByUserId(currentUser.getUserId()).getDoctorId();
            
            if (patientId != null) {
                prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
                // 过滤出该医生开具的处方
                prescriptions.removeIf(p -> !p.getDoctorId().equals(userDoctorId));
            } else {
                prescriptions = prescriptionService.getPrescriptionsByDoctorId(userDoctorId);
            }
            
        } else if (isAdmin(request)) {
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
            return Result.forbidden();
        }
        
        // 如果指定了status参数，进一步过滤
        if (status != null && !isAdmin(request)) {
            prescriptions.removeIf(p -> !p.getStatus().equals(status));
        }
        
        return Result.success(prescriptions);
    }

    /**
     * 获取处方详情
     */
    @GetMapping("/{prescriptionId}")
    public Result getPrescriptionDetail(@PathVariable Integer prescriptionId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 获取处方详情
        Map<String, Object> prescriptionDetail = prescriptionService.getPrescriptionDetail(prescriptionId);
        if (prescriptionDetail == null) {
            return Result.error("处方不存在");
        }
        
        Prescription prescription = (Prescription) prescriptionDetail.get("prescription");
        
        // 检查权限
        if (isPatient(request)) {
            // 患者只能查看自己的处方
            Integer userPatientId = patientService.getPatientByUserId(currentUser.getUserId()).getPatientId();
            if (!prescription.getPatientId().equals(userPatientId)) {
                return Result.forbidden();
            }
        } else if (isDoctor(request)) {
            // 医生只能查看自己开具的处方
            Integer userDoctorId = doctorService.getDoctorByUserId(currentUser.getUserId()).getDoctorId();
            if (!prescription.getDoctorId().equals(userDoctorId)) {
                return Result.forbidden();
            }
        } else if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        return Result.success(prescriptionDetail);
    }

    /**
     * 更新处方状态
     */
    @PutMapping("/{prescriptionId}/status")
    public Result updatePrescriptionStatus(@PathVariable Integer prescriptionId, @RequestBody String status, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有医生和管理员可以更新处方状态
        if (!isDoctor(request) && !isAdmin(request)) {
            return Result.forbidden();
        }
        
        // 验证状态值
        if (!"待发药".equals(status) && !"已发药".equals(status) && !"已取消".equals(status)) {
            return Result.validateFailed("无效的处方状态");
        }
        
        // 如果是医生，需要验证是该医生开具的处方
        if (isDoctor(request)) {
            Prescription prescription = prescriptionService.getPrescriptionById(prescriptionId);
            if (prescription == null) {
                return Result.error("处方不存在");
            }
            
            Integer userDoctorId = doctorService.getDoctorByUserId(currentUser.getUserId()).getDoctorId();
            if (!prescription.getDoctorId().equals(userDoctorId)) {
                return Result.forbidden();
            }
        }
        
        // 更新处方状态
        boolean result = prescriptionService.updatePrescriptionStatus(prescriptionId, status);
        if (result) {
            return Result.success("处方状态更新成功");
        } else {
            return Result.error("处方状态更新失败");
        }
    }
    
    /**
     * 获取药品列表（供医生开处方使用）
     */
    @GetMapping("/medicines")
    public Result getMedicineList(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 获取药品列表
        List<Medicine> medicines = medicineService.getAllMedicines();
        
        return Result.success(medicines);
    }
} 