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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 病历控制器
 */
@RestController
@RequestMapping("/api/medical-record")
public class MedicalRecordController extends BaseController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private EyeExaminationService eyeExaminationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取病历列表
     * @param patientId 患者ID（医生和管理员必填，患者不填则查询自己）
     * @param doctorId 医生ID（可选）
     * @param status 状态（可选）
     * @param request HTTP请求
     * @return 病历列表
     */
    @GetMapping("/list")
    public Result getMedicalRecords(
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色和参数判断查询哪些病历
        List<MedicalRecord> medicalRecords = null;
        
        if (isPatient(request)) {
            // 患者只能查看自己的病历
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            
            if (patientId != null && !patient.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
            
            medicalRecords = medicalRecordService.getMedicalRecordsByPatientId(patient.getPatientId());
            
            // 如果指定了医生ID，则在内存中过滤
            if (doctorId != null) {
                medicalRecords.removeIf(m -> !m.getDoctorId().equals(doctorId));
            }
            
            // 如果指定了状态，则在内存中过滤
            if (status != null && !status.isEmpty()) {
                medicalRecords.removeIf(m -> !m.getStatus().equals(status));
            }
        } else if (isDoctor(request)) {
            // 医生可以查看自己负责的病历，或者指定患者的病历
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null) {
                return Result.error("医生信息不存在");
            }
            
            if (patientId != null) {
                // 查询指定患者的病历
                medicalRecords = medicalRecordService.getMedicalRecordsByPatientId(patientId);
                // 如果医生查询其他医生的患者病历，则只返回自己负责的病历
                medicalRecords.removeIf(m -> !m.getDoctorId().equals(doctor.getDoctorId()));
            } else {
                // 查询医生自己负责的所有病历
                medicalRecords = medicalRecordService.getMedicalRecordsByDoctorId(doctor.getDoctorId());
            }
            
            // 如果指定了状态，则在内存中过滤
            if (status != null && !status.isEmpty()) {
                medicalRecords.removeIf(m -> !m.getStatus().equals(status));
            }
        } else if (isAdmin(request)) {
            // 管理员可以查看所有病历
            if (patientId != null) {
                // 查询指定患者的病历
                medicalRecords = medicalRecordService.getMedicalRecordsByPatientId(patientId);
                
                // 如果指定了医生ID，则在内存中过滤
                if (doctorId != null) {
                    medicalRecords.removeIf(m -> !m.getDoctorId().equals(doctorId));
                }
            } else if (doctorId != null) {
                // 查询指定医生的病历
                medicalRecords = medicalRecordService.getMedicalRecordsByDoctorId(doctorId);
            } else if (status != null && !status.isEmpty()) {
                // 查询指定状态的病历
                medicalRecords = medicalRecordService.getMedicalRecordsByStatus(status);
            } else {
                // 查询所有病历（这里需要自定义查询方法，暂时返回错误信息）
                return Result.validateFailed("请指定查询条件");
            }
        } else {
            return Result.forbidden();
        }

        return Result.success(medicalRecords);
    }

    /**
     * 获取病历详情
     * @param recordId 病历ID
     * @param request HTTP请求
     * @return 病历详情
     */
    @GetMapping("/{recordId}")
    public Result getMedicalRecord(@PathVariable Integer recordId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询病历
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordById(recordId);
        if (medicalRecord == null) {
            return Result.error("病历不存在");
        }

        // 根据角色判断是否有权限查看
        if (isPatient(request)) {
            // 患者只能查看自己的病历
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || !patient.getPatientId().equals(medicalRecord.getPatientId())) {
                return Result.forbidden();
            }
        } else if (isDoctor(request)) {
            // 医生只能查看自己负责的病历
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(medicalRecord.getDoctorId())) {
                return Result.forbidden();
            }
        } else if (isAdmin(request)) {
            // 管理员可以查看所有病历
        } else {
            return Result.forbidden();
        }

        // 查询相关的眼科检查记录
        List<EyeExamination> examinations = eyeExaminationService.getEyeExaminationsByRecordId(recordId);

        // 组装返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("record", medicalRecord);
        data.put("examinations", examinations);

        return Result.success(data);
    }

    /**
     * 获取眼科检查记录列表
     * @param patientId 患者ID（医生和管理员必填，患者不填则查询自己）
     * @param request HTTP请求
     * @return 眼科检查记录列表
     */
    @GetMapping("/examinations")
    public Result getEyeExaminations(@RequestParam(required = false) Integer patientId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色和参数判断查询哪个患者的眼科检查记录
        List<EyeExamination> examinations = null;
        if (isPatient(request)) {
            // 患者只能查看自己的眼科检查记录
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            
            if (patientId != null && !patient.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
            
            examinations = eyeExaminationService.getEyeExaminationsByPatientId(patient.getPatientId());
        } else if (isDoctor(request)) {
            // 医生可以查看指定患者的眼科检查记录，或者查看自己负责的所有眼科检查记录
            if (patientId != null) {
                examinations = eyeExaminationService.getEyeExaminationsByPatientId(patientId);
            } else {
                // 这里需要先获取医生ID，然后查询医生负责的眼科检查记录
                Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
                if (doctor == null) {
                    return Result.error("医生信息不存在");
                }
                examinations = eyeExaminationService.getEyeExaminationsByDoctorId(doctor.getDoctorId());
            }
        } else if (isAdmin(request)) {
            // 管理员可以查看所有眼科检查记录，但需要指定患者ID
            if (patientId == null) {
                return Result.validateFailed("请指定患者ID");
            }
            examinations = eyeExaminationService.getEyeExaminationsByPatientId(patientId);
        } else {
            return Result.forbidden();
        }

        return Result.success(examinations);
    }

    /**
     * 获取眼科检查记录详情
     * @param examinationId 眼科检查记录ID
     * @param request HTTP请求
     * @return 眼科检查记录详情
     */
    @GetMapping("/examination/{examinationId}")
    public Result getEyeExamination(@PathVariable Integer examinationId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询眼科检查记录
        EyeExamination examination = eyeExaminationService.getEyeExaminationById(examinationId);
        if (examination == null) {
            return Result.error("眼科检查记录不存在");
        }

        // 根据角色判断是否有权限查看
        if (isPatient(request)) {
            // 患者只能查看自己的眼科检查记录
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || !patient.getPatientId().equals(examination.getPatientId())) {
                return Result.forbidden();
            }
        } else if (isDoctor(request)) {
            // 医生可以查看自己负责的眼科检查记录，或者所有眼科检查记录（简化处理）
            // 这里简化处理，允许医生查看所有眼科检查记录
        } else if (isAdmin(request)) {
            // 管理员可以查看所有眼科检查记录
        } else {
            return Result.forbidden();
        }

        return Result.success(examination);
    }

    /**
     * 添加病历（仅医生）
     * @param medicalRecord 病历信息
     * @param request HTTP请求
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addMedicalRecord(@RequestBody MedicalRecord medicalRecord, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生可以添加病历
        if (!isDoctor(request)) {
            return Result.forbidden();
        }

        // 验证病历信息
        if (medicalRecord.getPatientId() == null) {
            return Result.validateFailed("患者ID不能为空");
        }

        // 设置医生ID为当前登录医生
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null) {
            return Result.error("医生信息不存在");
        }
        medicalRecord.setDoctorId(doctor.getDoctorId());

        // 添加病历
        boolean success = medicalRecordService.addMedicalRecord(medicalRecord);
        if (success) {
            return Result.success(medicalRecord);
        } else {
            return Result.error("添加病历失败");
        }
    }

    /**
     * 更新病历（仅医生）
     * @param medicalRecord 病历信息
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result updateMedicalRecord(@RequestBody MedicalRecord medicalRecord, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生可以更新病历
        if (!isDoctor(request)) {
            return Result.forbidden();
        }

        // 验证病历ID
        if (medicalRecord.getRecordId() == null) {
            return Result.validateFailed("病历ID不能为空");
        }

        // 查询原病历信息
        MedicalRecord originalMedicalRecord = medicalRecordService.getMedicalRecordById(medicalRecord.getRecordId());
        if (originalMedicalRecord == null) {
            return Result.error("病历不存在");
        }

        // 医生只能更新自己负责的病历
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null || !doctor.getDoctorId().equals(originalMedicalRecord.getDoctorId())) {
            return Result.forbidden();
        }

        // 不允许修改患者ID和医生ID
        medicalRecord.setPatientId(originalMedicalRecord.getPatientId());
        medicalRecord.setDoctorId(originalMedicalRecord.getDoctorId());

        // 更新病历
        boolean success = medicalRecordService.updateMedicalRecord(medicalRecord);
        if (success) {
            return Result.success(medicalRecord);
        } else {
            return Result.error("更新病历失败");
        }
    }

    /**
     * 更新病历状态为待诊（仅医生）
     * @param recordId 病历ID
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/{recordId}/pending")
    public Result setMedicalRecordPending(@PathVariable Integer recordId, HttpServletRequest request) {
        return updateMedicalRecordStatus(recordId, "待诊", request);
    }

    /**
     * 更新病历状态为诊疗中（仅医生）
     * @param recordId 病历ID
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/{recordId}/in-progress")
    public Result setMedicalRecordInProgress(@PathVariable Integer recordId, HttpServletRequest request) {
        return updateMedicalRecordStatus(recordId, "诊疗中", request);
    }

    /**
     * 更新病历状态为已完成（仅医生）
     * @param recordId 病历ID
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/{recordId}/completed")
    public Result setMedicalRecordCompleted(@PathVariable Integer recordId, HttpServletRequest request) {
        return updateMedicalRecordStatus(recordId, "已完成", request);
    }

    /**
     * 更新病历状态为已取消（仅医生）
     * @param recordId 病历ID
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/{recordId}/cancelled")
    public Result setMedicalRecordCancelled(@PathVariable Integer recordId, HttpServletRequest request) {
        return updateMedicalRecordStatus(recordId, "已取消", request);
    }

    /**
     * 更新病历状态
     * @param recordId 病历ID
     * @param status 状态
     * @param request HTTP请求
     * @return 更新结果
     */
    private Result updateMedicalRecordStatus(Integer recordId, String status, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生可以更新病历状态
        if (!isDoctor(request)) {
            return Result.forbidden();
        }

        // 查询病历
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordById(recordId);
        if (medicalRecord == null) {
            return Result.error("病历不存在");
        }

        // 医生只能更新自己负责的病历
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null || !doctor.getDoctorId().equals(medicalRecord.getDoctorId())) {
            return Result.forbidden();
        }

        // 更新病历状态
        boolean success = false;
        if ("待诊".equals(status)) {
            success = medicalRecordService.setMedicalRecordPending(recordId);
        } else if ("诊疗中".equals(status)) {
            success = medicalRecordService.setMedicalRecordInProgress(recordId);
        } else if ("已完成".equals(status)) {
            success = medicalRecordService.setMedicalRecordCompleted(recordId);
        } else if ("已取消".equals(status)) {
            success = medicalRecordService.setMedicalRecordCancelled(recordId);
        }

        if (success) {
            return Result.success(null);
        } else {
            return Result.error("更新病历状态失败");
        }
    }
} 