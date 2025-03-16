package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.entity.EyeExamination;
import com.example.shijiehouduan.entity.MedicalRecord;
import com.example.shijiehouduan.entity.Patient;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.EyeExaminationService;
import com.example.shijiehouduan.service.MedicalRecordService;
import com.example.shijiehouduan.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 眼科检查控制器
 */
@RestController
@RequestMapping("/api/eye-examination")
public class EyeExaminationController {

    @Autowired
    private EyeExaminationService eyeExaminationService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取眼科检查记录列表
     * @param patientId 患者ID（可选）
     * @param doctorId 医生ID（可选）
     * @param recordId 病历ID（可选）
     * @param session HTTP会话
     * @return 眼科检查记录列表
     */
    @GetMapping("/list")
    public Result getEyeExaminations(
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer recordId,
            HttpSession session) {
        
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色和参数判断查询哪些眼科检查记录
        List<EyeExamination> examinations = null;
        
        if ("患者".equals(currentUser.getRoleType())) {
            // 患者只能查看自己的眼科检查记录
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            
            if (patientId != null && !patient.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
            
            examinations = eyeExaminationService.getEyeExaminationsByPatientId(patient.getPatientId());
            
            // 如果指定了医生ID，则在内存中过滤
            if (doctorId != null) {
                examinations.removeIf(e -> !e.getDoctorId().equals(doctorId));
            }
            
            // 如果指定了病历ID，则在内存中过滤
            if (recordId != null) {
                examinations.removeIf(e -> !e.getRecordId().equals(recordId));
            }
        } else if ("医生".equals(currentUser.getRoleType())) {
            // 医生可以查看自己负责的眼科检查记录，或者指定患者的眼科检查记录
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null) {
                return Result.error("医生信息不存在");
            }
            
            if (patientId != null) {
                // 查询指定患者的眼科检查记录
                examinations = eyeExaminationService.getEyeExaminationsByPatientId(patientId);
                // 如果医生查询其他医生的患者眼科检查记录，则只返回自己负责的眼科检查记录
                examinations.removeIf(e -> !e.getDoctorId().equals(doctor.getDoctorId()));
            } else if (recordId != null) {
                // 查询指定病历的眼科检查记录
                examinations = eyeExaminationService.getEyeExaminationsByRecordId(recordId);
                // 如果医生查询其他医生的病历眼科检查记录，则只返回自己负责的眼科检查记录
                examinations.removeIf(e -> !e.getDoctorId().equals(doctor.getDoctorId()));
            } else {
                // 查询医生自己负责的所有眼科检查记录
                examinations = eyeExaminationService.getEyeExaminationsByDoctorId(doctor.getDoctorId());
            }
        } else if ("管理员".equals(currentUser.getRoleType())) {
            // 管理员可以查看所有眼科检查记录
            if (patientId != null) {
                // 查询指定患者的眼科检查记录
                examinations = eyeExaminationService.getEyeExaminationsByPatientId(patientId);
                
                // 如果指定了医生ID，则在内存中过滤
                if (doctorId != null) {
                    examinations.removeIf(e -> !e.getDoctorId().equals(doctorId));
                }
                
                // 如果指定了病历ID，则在内存中过滤
                if (recordId != null) {
                    examinations.removeIf(e -> !e.getRecordId().equals(recordId));
                }
            } else if (doctorId != null) {
                // 查询指定医生的眼科检查记录
                examinations = eyeExaminationService.getEyeExaminationsByDoctorId(doctorId);
                
                // 如果指定了病历ID，则在内存中过滤
                if (recordId != null) {
                    examinations.removeIf(e -> !e.getRecordId().equals(recordId));
                }
            } else if (recordId != null) {
                // 查询指定病历的眼科检查记录
                examinations = eyeExaminationService.getEyeExaminationsByRecordId(recordId);
            } else {
                // 查询所有眼科检查记录（这里需要自定义查询方法，暂时返回错误信息）
                return Result.validateFailed("请指定查询条件");
            }
        } else {
            return Result.forbidden();
        }

        return Result.success(examinations);
    }

    /**
     * 获取眼科检查记录详情
     * @param examinationId 眼科检查记录ID
     * @param session HTTP会话
     * @return 眼科检查记录详情
     */
    @GetMapping("/{examinationId}")
    public Result getEyeExamination(@PathVariable Integer examinationId, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询眼科检查记录
        EyeExamination examination = eyeExaminationService.getEyeExaminationById(examinationId);
        if (examination == null) {
            return Result.error("眼科检查记录不存在");
        }

        // 根据角色判断是否有权限查看
        if ("患者".equals(currentUser.getRoleType())) {
            // 患者只能查看自己的眼科检查记录
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || !patient.getPatientId().equals(examination.getPatientId())) {
                return Result.forbidden();
            }
        } else if ("医生".equals(currentUser.getRoleType())) {
            // 医生只能查看自己负责的眼科检查记录
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(examination.getDoctorId())) {
                return Result.forbidden();
            }
        } else if ("管理员".equals(currentUser.getRoleType())) {
            // 管理员可以查看所有眼科检查记录
        } else {
            return Result.forbidden();
        }

        return Result.success(examination);
    }

    /**
     * 添加眼科检查记录（仅医生）
     * @param examination 眼科检查记录信息
     * @param session HTTP会话
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addEyeExamination(@RequestBody EyeExamination examination, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生可以添加眼科检查记录
        if (!"医生".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 验证眼科检查记录信息
        if (examination.getPatientId() == null) {
            return Result.validateFailed("患者ID不能为空");
        }
        
        if (examination.getRecordId() == null) {
            return Result.validateFailed("病历ID不能为空");
        }
        
        // 验证病历是否存在
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordById(examination.getRecordId());
        if (medicalRecord == null) {
            return Result.error("病历不存在");
        }
        
        // 验证病历是否属于指定患者
        if (!medicalRecord.getPatientId().equals(examination.getPatientId())) {
            return Result.validateFailed("病历与患者不匹配");
        }

        // 设置医生ID为当前登录医生
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null) {
            return Result.error("医生信息不存在");
        }
        examination.setDoctorId(doctor.getDoctorId());
        
        // 设置检查日期
        if (examination.getExaminationDate() == null) {
            examination.setExaminationDate(new Date());
        }
        
        // 设置创建时间
        examination.setCreatedAt(new Date());

        // 添加眼科检查记录
        boolean success = eyeExaminationService.addEyeExamination(examination);
        if (success) {
            return Result.success(examination);
        } else {
            return Result.error("添加眼科检查记录失败");
        }
    }

    /**
     * 更新眼科检查记录（仅医生）
     * @param examination 眼科检查记录信息
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result updateEyeExamination(@RequestBody EyeExamination examination, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生可以更新眼科检查记录
        if (!"医生".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 验证眼科检查记录ID
        if (examination.getExaminationId() == null) {
            return Result.validateFailed("眼科检查记录ID不能为空");
        }

        // 查询原眼科检查记录信息
        EyeExamination originalExamination = eyeExaminationService.getEyeExaminationById(examination.getExaminationId());
        if (originalExamination == null) {
            return Result.error("眼科检查记录不存在");
        }

        // 医生只能更新自己负责的眼科检查记录
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null || !doctor.getDoctorId().equals(originalExamination.getDoctorId())) {
            return Result.forbidden();
        }

        // 不允许修改患者ID、医生ID和病历ID
        examination.setPatientId(originalExamination.getPatientId());
        examination.setDoctorId(originalExamination.getDoctorId());
        examination.setRecordId(originalExamination.getRecordId());

        // 更新眼科检查记录
        boolean success = eyeExaminationService.updateEyeExamination(examination);
        if (success) {
            return Result.success(examination);
        } else {
            return Result.error("更新眼科检查记录失败");
        }
    }
} 