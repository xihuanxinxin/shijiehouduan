package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Bed;
import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.entity.Hospitalization;
import com.example.shijiehouduan.entity.Patient;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.BedService;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.HospitalizationService;
import com.example.shijiehouduan.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 住院信息控制器
 */
@RestController
@RequestMapping("/api/hospitalization")
public class HospitalizationController {

    @Autowired
    private HospitalizationService hospitalizationService;

    @Autowired
    private BedService bedService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取住院信息列表
     * @param patientId 患者ID（可选）
     * @param doctorId 医生ID（可选）
     * @param bedId 床位ID（可选）
     * @param session HTTP会话
     * @return 住院信息列表
     */
    @GetMapping("/list")
    public Result getHospitalizations(
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer bedId,
            HttpSession session) {
        
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色和参数判断查询哪些住院信息
        List<Hospitalization> hospitalizations = null;
        
        if ("患者".equals(currentUser.getRoleType())) {
            // 患者只能查看自己的住院信息
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            
            if (patientId != null && !patient.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
            
            hospitalizations = hospitalizationService.getHospitalizationsByPatientId(patient.getPatientId());
            
            // 如果指定了医生ID，则在内存中过滤
            if (doctorId != null) {
                hospitalizations.removeIf(h -> !h.getDoctorId().equals(doctorId));
            }
            
            // 如果指定了床位ID，则在内存中过滤
            if (bedId != null) {
                hospitalizations.removeIf(h -> !h.getBedId().equals(bedId));
            }
        } else if ("医生".equals(currentUser.getRoleType())) {
            // 医生可以查看自己负责的住院信息，或者指定患者的住院信息
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null) {
                return Result.error("医生信息不存在");
            }
            
            if (patientId != null) {
                // 查询指定患者的住院信息
                hospitalizations = hospitalizationService.getHospitalizationsByPatientId(patientId);
                // 如果医生查询其他医生的患者住院信息，则只返回自己负责的住院信息
                hospitalizations.removeIf(h -> !h.getDoctorId().equals(doctor.getDoctorId()));
            } else if (bedId != null) {
                // 查询指定床位的住院信息
                Hospitalization hospitalization = hospitalizationService.getHospitalizationByBedId(bedId);
                if (hospitalization != null && hospitalization.getDoctorId().equals(doctor.getDoctorId())) {
                    hospitalizations = Collections.singletonList(hospitalization);
                } else {
                    hospitalizations = Collections.emptyList();
                }
            } else {
                // 查询医生自己负责的所有住院信息
                hospitalizations = hospitalizationService.getHospitalizationsByDoctorId(doctor.getDoctorId());
            }
        } else if ("管理员".equals(currentUser.getRoleType())) {
            // 管理员可以查看所有住院信息
            if (patientId != null) {
                // 查询指定患者的住院信息
                hospitalizations = hospitalizationService.getHospitalizationsByPatientId(patientId);
                
                // 如果指定了医生ID，则在内存中过滤
                if (doctorId != null) {
                    hospitalizations.removeIf(h -> !h.getDoctorId().equals(doctorId));
                }
                
                // 如果指定了床位ID，则在内存中过滤
                if (bedId != null) {
                    hospitalizations.removeIf(h -> !h.getBedId().equals(bedId));
                }
            } else if (doctorId != null) {
                // 查询指定医生的住院信息
                hospitalizations = hospitalizationService.getHospitalizationsByDoctorId(doctorId);
                
                // 如果指定了床位ID，则在内存中过滤
                if (bedId != null) {
                    hospitalizations.removeIf(h -> !h.getBedId().equals(bedId));
                }
            } else if (bedId != null) {
                // 查询指定床位的住院信息
                Hospitalization hospitalization = hospitalizationService.getHospitalizationByBedId(bedId);
                if (hospitalization != null) {
                    hospitalizations = Collections.singletonList(hospitalization);
                } else {
                    hospitalizations = Collections.emptyList();
                }
            } else {
                // 查询所有当前住院信息
                hospitalizations = hospitalizationService.getCurrentHospitalizations();
            }
        } else {
            return Result.forbidden();
        }

        return Result.success(hospitalizations);
    }

    /**
     * 获取住院信息详情
     * @param hospitalizationId 住院信息ID
     * @param session HTTP会话
     * @return 住院信息详情
     */
    @GetMapping("/{hospitalizationId}")
    public Result getHospitalization(@PathVariable Integer hospitalizationId, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询住院信息
        Hospitalization hospitalization = hospitalizationService.getHospitalizationById(hospitalizationId);
        if (hospitalization == null) {
            return Result.error("住院信息不存在");
        }

        // 根据角色判断是否有权限查看
        if ("患者".equals(currentUser.getRoleType())) {
            // 患者只能查看自己的住院信息
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || !patient.getPatientId().equals(hospitalization.getPatientId())) {
                return Result.forbidden();
            }
        } else if ("医生".equals(currentUser.getRoleType())) {
            // 医生只能查看自己负责的住院信息
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(hospitalization.getDoctorId())) {
                return Result.forbidden();
            }
        } else if ("管理员".equals(currentUser.getRoleType())) {
            // 管理员可以查看所有住院信息
        } else {
            return Result.forbidden();
        }

        return Result.success(hospitalization);
    }

    /**
     * 获取当前住院信息
     * @param session HTTP会话
     * @return 当前住院信息列表
     */
    @GetMapping("/current")
    public Result getCurrentHospitalizations(HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色判断查询哪些住院信息
        List<Hospitalization> hospitalizations = null;
        
        if ("患者".equals(currentUser.getRoleType())) {
            // 患者只能查看自己的住院信息
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            
            hospitalizations = hospitalizationService.getHospitalizationsByPatientId(patient.getPatientId());
            // 过滤出当前住院的记录（未出院的）
            hospitalizations.removeIf(h -> h.getDischargeDate() != null);
        } else if ("医生".equals(currentUser.getRoleType())) {
            // 医生可以查看自己负责的当前住院信息
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null) {
                return Result.error("医生信息不存在");
            }
            
            hospitalizations = hospitalizationService.getHospitalizationsByDoctorId(doctor.getDoctorId());
            // 过滤出当前住院的记录（未出院的）
            hospitalizations.removeIf(h -> h.getDischargeDate() != null);
        } else if ("管理员".equals(currentUser.getRoleType())) {
            // 管理员可以查看所有当前住院信息
            hospitalizations = hospitalizationService.getCurrentHospitalizations();
        } else {
            return Result.forbidden();
        }

        return Result.success(hospitalizations);
    }

    /**
     * 获取床位列表
     * @param status 床位状态（可选）
     * @param roomNumber 房间号（可选）
     * @param session HTTP会话
     * @return 床位列表
     */
    @GetMapping("/beds")
    public Result getBeds(@RequestParam(required = false) String status, 
                          @RequestParam(required = false) String roomNumber,
                          HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以查看所有床位
        if (!"医生".equals(currentUser.getRoleType()) && !"管理员".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        List<Bed> beds = null;
        if (status != null) {
            // 根据状态查询床位
            beds = bedService.getBedsByStatus(status);
        } else if (roomNumber != null) {
            // 根据房间号查询床位
            beds = bedService.getBedsByRoomNumber(roomNumber);
        } else {
            // 查询所有床位
            beds = bedService.getAllBeds();
        }

        return Result.success(beds);
    }

    /**
     * 获取床位详情
     * @param bedId 床位ID
     * @param session HTTP会话
     * @return 床位详情
     */
    @GetMapping("/bed/{bedId}")
    public Result getBed(@PathVariable Integer bedId, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询床位
        Bed bed = bedService.getBedById(bedId);
        if (bed == null) {
            return Result.error("床位不存在");
        }

        // 患者只能查看自己的床位
        if ("患者".equals(currentUser.getRoleType())) {
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || bed.getCurrentPatientId() == null || !bed.getCurrentPatientId().equals(patient.getPatientId())) {
                return Result.forbidden();
            }
        }

        // 查询床位对应的住院记录
        Hospitalization hospitalization = hospitalizationService.getHospitalizationByBedId(bedId);

        // 组装返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("bed", bed);
        data.put("hospitalization", hospitalization);

        return Result.success(data);
    }

    /**
     * 获取患者当前的床位
     * @param patientId 患者ID（患者不填则查询自己）
     * @param session HTTP会话
     * @return 患者当前的床位
     */
    @GetMapping("/patient/bed")
    public Result getPatientBed(@RequestParam(required = false) Integer patientId, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 确定要查询的患者ID
        Integer targetPatientId = null;
        if ("患者".equals(currentUser.getRoleType())) {
            // 患者只能查看自己的床位
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            
            if (patientId != null && !patient.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
            
            targetPatientId = patient.getPatientId();
        } else if ("医生".equals(currentUser.getRoleType()) || "管理员".equals(currentUser.getRoleType())) {
            // 医生和管理员可以查看指定患者的床位
            if (patientId == null) {
                return Result.validateFailed("请指定患者ID");
            }
            targetPatientId = patientId;
        } else {
            return Result.forbidden();
        }

        // 查询患者当前的床位
        Bed bed = bedService.getBedByCurrentPatientId(targetPatientId);
        if (bed == null) {
            return Result.error("患者当前没有床位");
        }

        // 查询床位对应的住院记录
        Hospitalization hospitalization = hospitalizationService.getHospitalizationByBedId(bed.getBedId());

        // 组装返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("bed", bed);
        data.put("hospitalization", hospitalization);

        return Result.success(data);
    }

    /**
     * 添加住院记录（仅医生和管理员）
     * @param hospitalization 住院记录信息
     * @param session HTTP会话
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addHospitalization(@RequestBody Hospitalization hospitalization, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以添加住院记录
        if (!"医生".equals(currentUser.getRoleType()) && !"管理员".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 验证住院记录信息
        if (hospitalization.getPatientId() == null) {
            return Result.validateFailed("患者ID不能为空");
        }
        
        if (hospitalization.getDoctorId() == null) {
            // 如果是医生添加，则设置医生ID为当前登录医生
            if ("医生".equals(currentUser.getRoleType())) {
                Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
                if (doctor == null) {
                    return Result.error("医生信息不存在");
                }
                hospitalization.setDoctorId(doctor.getDoctorId());
            } else {
                return Result.validateFailed("医生ID不能为空");
            }
        }
        
        if (hospitalization.getBedId() == null) {
            return Result.validateFailed("床位ID不能为空");
        }
        
        if (hospitalization.getAdmissionDate() == null) {
            return Result.validateFailed("入院日期不能为空");
        }
        
        if (hospitalization.getReason() == null || hospitalization.getReason().isEmpty()) {
            return Result.validateFailed("住院原因不能为空");
        }
        
        // 验证床位是否存在且空闲
        Bed bed = bedService.getBedById(hospitalization.getBedId());
        if (bed == null) {
            return Result.error("床位不存在");
        }
        
        if (!"空闲".equals(bed.getStatus())) {
            return Result.validateFailed("床位已被占用");
        }
        
        // 添加住院记录
        boolean success = hospitalizationService.addHospitalization(hospitalization);
        if (success) {
            return Result.success(hospitalization);
        } else {
            return Result.error("添加住院记录失败");
        }
    }

    /**
     * 更新住院记录（仅医生和管理员）
     * @param hospitalization 住院记录信息
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result updateHospitalization(@RequestBody Hospitalization hospitalization, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以更新住院记录
        if (!"医生".equals(currentUser.getRoleType()) && !"管理员".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 验证住院记录ID
        if (hospitalization.getHospitalizationId() == null) {
            return Result.validateFailed("住院记录ID不能为空");
        }

        // 查询原住院记录信息
        Hospitalization originalHospitalization = hospitalizationService.getHospitalizationById(hospitalization.getHospitalizationId());
        if (originalHospitalization == null) {
            return Result.error("住院记录不存在");
        }

        // 医生只能更新自己负责的住院记录
        if ("医生".equals(currentUser.getRoleType())) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(originalHospitalization.getDoctorId())) {
                return Result.forbidden();
            }
        }

        // 如果修改了床位，需要验证新床位是否存在且空闲
        if (hospitalization.getBedId() != null && !hospitalization.getBedId().equals(originalHospitalization.getBedId())) {
            Bed newBed = bedService.getBedById(hospitalization.getBedId());
            if (newBed == null) {
                return Result.error("新床位不存在");
            }
            
            if (!"空闲".equals(newBed.getStatus())) {
                return Result.validateFailed("新床位已被占用");
            }
        }

        // 更新住院记录
        boolean success = hospitalizationService.updateHospitalization(hospitalization);
        if (success) {
            return Result.success(hospitalization);
        } else {
            return Result.error("更新住院记录失败");
        }
    }

    /**
     * 更新住院记录状态为出院（仅医生和管理员）
     * @param hospitalizationId 住院记录ID
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/{hospitalizationId}/discharge")
    public Result dischargePatient(@PathVariable Integer hospitalizationId, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以更新住院记录状态
        if (!"医生".equals(currentUser.getRoleType()) && !"管理员".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 查询住院记录
        Hospitalization hospitalization = hospitalizationService.getHospitalizationById(hospitalizationId);
        if (hospitalization == null) {
            return Result.error("住院记录不存在");
        }

        // 医生只能更新自己负责的住院记录
        if ("医生".equals(currentUser.getRoleType())) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(hospitalization.getDoctorId())) {
                return Result.forbidden();
            }
        }

        // 更新住院记录状态为出院
        boolean success = hospitalizationService.setHospitalizationDischarged(hospitalizationId, new java.util.Date());
        if (success) {
            return Result.success(null);
        } else {
            return Result.error("更新住院记录状态失败");
        }
    }

    /**
     * 更新住院记录状态为转院（仅医生和管理员）
     * @param hospitalizationId 住院记录ID
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/{hospitalizationId}/transfer")
    public Result transferPatient(@PathVariable Integer hospitalizationId, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以更新住院记录状态
        if (!"医生".equals(currentUser.getRoleType()) && !"管理员".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 查询住院记录
        Hospitalization hospitalization = hospitalizationService.getHospitalizationById(hospitalizationId);
        if (hospitalization == null) {
            return Result.error("住院记录不存在");
        }

        // 医生只能更新自己负责的住院记录
        if ("医生".equals(currentUser.getRoleType())) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(hospitalization.getDoctorId())) {
                return Result.forbidden();
            }
        }

        // 更新住院记录状态为转院
        boolean success = hospitalizationService.setHospitalizationTransferred(hospitalizationId, new java.util.Date());
        if (success) {
            return Result.success(null);
        } else {
            return Result.error("更新住院记录状态失败");
        }
    }

    /**
     * 更新住院记录状态为死亡（仅医生和管理员）
     * @param hospitalizationId 住院记录ID
     * @param session HTTP会话
     * @return 更新结果
     */
    @PutMapping("/{hospitalizationId}/deceased")
    public Result deceasedPatient(@PathVariable Integer hospitalizationId, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以更新住院记录状态
        if (!"医生".equals(currentUser.getRoleType()) && !"管理员".equals(currentUser.getRoleType())) {
            return Result.forbidden();
        }

        // 查询住院记录
        Hospitalization hospitalization = hospitalizationService.getHospitalizationById(hospitalizationId);
        if (hospitalization == null) {
            return Result.error("住院记录不存在");
        }

        // 医生只能更新自己负责的住院记录
        if ("医生".equals(currentUser.getRoleType())) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(hospitalization.getDoctorId())) {
                return Result.forbidden();
            }
        }

        // 更新住院记录状态为死亡
        boolean success = hospitalizationService.setHospitalizationDeceased(hospitalizationId, new java.util.Date());
        if (success) {
            return Result.success(null);
        } else {
            return Result.error("更新住院记录状态失败");
        }
    }
} 