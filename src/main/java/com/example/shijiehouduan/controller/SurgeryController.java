package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.entity.MedicalRecord;
import com.example.shijiehouduan.entity.Patient;
import com.example.shijiehouduan.entity.Surgery;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.MedicalRecordService;
import com.example.shijiehouduan.service.PatientService;
import com.example.shijiehouduan.service.SurgeryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 手术记录控制器
 */
@RestController
@RequestMapping("/api/surgery")
public class SurgeryController {

    @Autowired
    private SurgeryService surgeryService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取手术记录列表
     * @param patientId 患者ID（可选）
     * @param doctorId 医生ID（可选）
     * @param recordId 病历ID（可选）
     * @param status 状态（可选）
     * @param session HTTP会话
     * @return 手术记录列表
     */
    @GetMapping("/list")
    public Result getSurgeries(
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer recordId,
            @RequestParam(required = false) String status,
            HttpSession session) {
        
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色和参数判断查询哪些手术记录
        List<Surgery> surgeries = null;
        
        if ("患者".equals(currentUser.getRoleType())) {
            // 患者只能查看自己的手术记录
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            
            if (patientId != null && !patient.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
            
            surgeries = surgeryService.getSurgeriesByPatientId(patient.getPatientId());
            
            // 如果指定了医生ID，则在内存中过滤
            if (doctorId != null) {
                surgeries.removeIf(s -> !s.getDoctorId().equals(doctorId));
            }
            
            // 如果指定了病历ID，则在内存中过滤
            if (recordId != null) {
                surgeries.removeIf(s -> !s.getRecordId().equals(recordId));
            }
            
            // 如果指定了状态，则在内存中过滤
            if (status != null && !status.isEmpty()) {
                surgeries.removeIf(s -> !s.getStatus().equals(status));
            }
        } else if ("医生".equals(currentUser.getRoleType())) {
            // 医生可以查看自己负责的手术记录，或者指定患者的手术记录
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null) {
                return Result.error("医生信息不存在");
            }
            
            if (patientId != null) {
                // 查询指定患者的手术记录
                surgeries = surgeryService.getSurgeriesByPatientId(patientId);
                // 如果医生查询其他医生的患者手术记录，则只返回自己负责的手术记录
                surgeries.removeIf(s -> !s.getDoctorId().equals(doctor.getDoctorId()));
            } else if (recordId != null) {
                // 查询指定病历的手术记录
                surgeries = surgeryService.getSurgeriesByRecordId(recordId);
                // 如果医生查询其他医生的病历手术记录，则只返回自己负责的手术记录
                surgeries.removeIf(s -> !s.getDoctorId().equals(doctor.getDoctorId()));
            } else if (status != null && !status.isEmpty()) {
                // 查询指定状态的手术记录
                surgeries = surgeryService.getSurgeriesByStatus(status);
                // 只返回自己负责的手术记录
                surgeries.removeIf(s -> !s.getDoctorId().equals(doctor.getDoctorId()));
            } else {
                // 查询医生自己负责的所有手术记录
                surgeries = surgeryService.getSurgeriesByDoctorId(doctor.getDoctorId());
            }
        } else if ("管理员".equals(currentUser.getRoleType())) {
            // 管理员可以查看所有手术记录
            if (patientId != null) {
                // 查询指定患者的手术记录
                surgeries = surgeryService.getSurgeriesByPatientId(patientId);
                
                // 如果指定了医生ID，则在内存中过滤
                if (doctorId != null) {
                    surgeries.removeIf(s -> !s.getDoctorId().equals(doctorId));
                }
                
                // 如果指定了病历ID，则在内存中过滤
                if (recordId != null) {
                    surgeries.removeIf(s -> !s.getRecordId().equals(recordId));
                }
                
                // 如果指定了状态，则在内存中过滤
                if (status != null && !status.isEmpty()) {
                    surgeries.removeIf(s -> !s.getStatus().equals(status));
                }
            } else if (doctorId != null) {
                // 查询指定医生的手术记录
                surgeries = surgeryService.getSurgeriesByDoctorId(doctorId);
                
                // 如果指定了病历ID，则在内存中过滤
                if (recordId != null) {
                    surgeries.removeIf(s -> !s.getRecordId().equals(recordId));
                }
                
                // 如果指定了状态，则在内存中过滤
                if (status != null && !status.isEmpty()) {
                    surgeries.removeIf(s -> !s.getStatus().equals(status));
                }
            } else if (recordId != null) {
                // 查询指定病历的手术记录
                surgeries = surgeryService.getSurgeriesByRecordId(recordId);
                
                // 如果指定了状态，则在内存中过滤
                if (status != null && !status.isEmpty()) {
                    surgeries.removeIf(s -> !s.getStatus().equals(status));
                }
            } else if (status != null && !status.isEmpty()) {
                // 查询指定状态的手术记录
                surgeries = surgeryService.getSurgeriesByStatus(status);
            } else {
                // 查询所有手术记录（这里需要自定义查询方法，暂时返回错误信息）
                return Result.validateFailed("请指定查询条件");
            }
        } else {
            return Result.forbidden();
        }

        return Result.success(surgeries);
    }

    /**
     * 获取手术记录详情
     * @param surgeryId 手术记录ID
     * @param session HTTP会话
     * @return 手术记录详情
     */
    @GetMapping("/{surgeryId}")
    public Result getSurgery(@PathVariable Integer surgeryId, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询手术记录
        Surgery surgery = surgeryService.getSurgeryById(surgeryId);
        if (surgery == null) {
            return Result.error("手术记录不存在");
        }

        // 根据角色判断是否有权限查看
        if ("患者".equals(currentUser.getRoleType())) {
            // 患者只能查看自己的手术记录
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || !patient.getPatientId().equals(surgery.getPatientId())) {
                return Result.forbidden();
            }
        } else if ("医生".equals(currentUser.getRoleType())) {
            // 医生只能查看自己负责的手术记录
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(surgery.getDoctorId())) {
                return Result.forbidden();
            }
        } else if ("管理员".equals(currentUser.getRoleType())) {
            // 管理员可以查看所有手术记录
        } else {
            return Result.forbidden();
        }

        return Result.success(surgery);
    }

    /**
     * 添加手术记录（仅医生）
     * @param surgery 手术记录信息
     * @param session HTTP会话
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addSurgery(@RequestBody Surgery surgery, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生可以添加手术记录
        if (!"医生".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 验证手术记录信息
        if (surgery.getPatientId() == null) {
            return Result.validateFailed("患者ID不能为空");
        }
        
        if (surgery.getRecordId() == null) {
            return Result.validateFailed("病历ID不能为空");
        }
        
        if (surgery.getSurgeryType() == null || surgery.getSurgeryType().isEmpty()) {
            return Result.validateFailed("手术类型不能为空");
        }
        
        if (surgery.getSurgeryDate() == null) {
            return Result.validateFailed("手术日期不能为空");
        }
        
        // 验证病历是否存在
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordById(surgery.getRecordId());
        if (medicalRecord == null) {
            return Result.error("病历不存在");
        }
        
        // 验证病历是否属于指定患者
        if (!medicalRecord.getPatientId().equals(surgery.getPatientId())) {
            return Result.validateFailed("病历与患者不匹配");
        }

        // 设置医生ID为当前登录医生
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null) {
            return Result.error("医生信息不存在");
        }
        surgery.setDoctorId(doctor.getDoctorId());
        
        // 设置创建时间
        surgery.setCreatedAt(new Date());

        // 添加手术记录
        boolean success = surgeryService.addSurgery(surgery);
        if (success) {
            return Result.success(surgery);
        } else {
            return Result.error("添加手术记录失败");
        }
    }

    /**
     * 更新手术记录（仅医生）
     * @param surgery 手术记录信息
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result updateSurgery(@RequestBody Surgery surgery, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生可以更新手术记录
        if (!"医生".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 验证手术记录ID
        if (surgery.getSurgeryId() == null) {
            return Result.validateFailed("手术记录ID不能为空");
        }

        // 查询原手术记录信息
        Surgery originalSurgery = surgeryService.getSurgeryById(surgery.getSurgeryId());
        if (originalSurgery == null) {
            return Result.error("手术记录不存在");
        }

        // 医生只能更新自己负责的手术记录
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null || !doctor.getDoctorId().equals(originalSurgery.getDoctorId())) {
            return Result.forbidden();
        }

        // 不允许修改患者ID、医生ID和病历ID
        surgery.setPatientId(originalSurgery.getPatientId());
        surgery.setDoctorId(originalSurgery.getDoctorId());
        surgery.setRecordId(originalSurgery.getRecordId());

        // 更新手术记录
        boolean success = surgeryService.updateSurgery(surgery);
        if (success) {
            return Result.success(surgery);
        } else {
            return Result.error("更新手术记录失败");
        }
    }

    /**
     * 更新手术记录状态为待手术（仅医生）
     * @param surgeryId 手术记录ID
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/{surgeryId}/pending")
    public Result setSurgeryPending(@PathVariable Integer surgeryId, HttpSession session) {
        return updateSurgeryStatus(surgeryId, "待手术", session);
    }

    /**
     * 更新手术记录状态为手术中（仅医生）
     * @param surgeryId 手术记录ID
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/{surgeryId}/in-progress")
    public Result setSurgeryInProgress(@PathVariable Integer surgeryId, HttpSession session) {
        return updateSurgeryStatus(surgeryId, "手术中", session);
    }

    /**
     * 更新手术记录状态为已完成（仅医生）
     * @param surgeryId 手术记录ID
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/{surgeryId}/completed")
    public Result setSurgeryCompleted(@PathVariable Integer surgeryId, HttpSession session) {
        return updateSurgeryStatus(surgeryId, "已完成", session);
    }

    /**
     * 更新手术记录状态为已取消（仅医生）
     * @param surgeryId 手术记录ID
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/{surgeryId}/cancelled")
    public Result setSurgeryCancelled(@PathVariable Integer surgeryId, HttpSession session) {
        return updateSurgeryStatus(surgeryId, "已取消", session);
    }

    /**
     * 更新手术记录状态
     * @param surgeryId 手术记录ID
     * @param status 状态
     * @param session HTTP会话
     * @return 更新结果
     */
    private Result updateSurgeryStatus(Integer surgeryId, String status, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生可以更新手术记录状态
        if (!"医生".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 查询手术记录
        Surgery surgery = surgeryService.getSurgeryById(surgeryId);
        if (surgery == null) {
            return Result.error("手术记录不存在");
        }

        // 医生只能更新自己负责的手术记录
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null || !doctor.getDoctorId().equals(surgery.getDoctorId())) {
            return Result.forbidden();
        }

        // 更新手术记录状态
        boolean success = surgeryService.updateSurgeryStatus(surgeryId, status);

        if (success) {
            return Result.success(null);
        } else {
            return Result.error("更新手术记录状态失败");
        }
    }
} 