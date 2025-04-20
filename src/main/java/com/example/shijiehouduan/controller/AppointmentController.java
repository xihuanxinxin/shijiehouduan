package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Appointment;
import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.entity.Patient;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.AppointmentService;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 预约挂号控制器
 */
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController extends BaseController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取预约列表
     * @param patientId 患者ID（医生和管理员必填，患者不填则查询自己）
     * @param doctorId 医生ID（可选）
     * @param status 状态（可选）
     * @param request HTTP请求
     * @return 预约列表
     */
    @GetMapping("/list")
    public Result getAppointments(
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 根据角色和参数判断查询哪些预约
        List<Appointment> appointments = null;
        
        if (isPatient(request)) {
            // 患者只能查看自己的预约
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            
            if (patientId != null && !patient.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
            
            // 如果指定了医生ID，则查询该患者与该医生的预约
            if (doctorId != null) {
                // 这里需要自定义查询方法，暂时使用患者ID查询后在内存中过滤
                appointments = appointmentService.getAppointmentsByPatientId(patient.getPatientId());
                appointments.removeIf(a -> !a.getDoctorId().equals(doctorId));
            } else {
                // 否则查询该患者的所有预约
                appointments = appointmentService.getAppointmentsByPatientId(patient.getPatientId());
            }
            
            // 如果指定了状态，则在内存中过滤
            if (status != null && !status.isEmpty()) {
                appointments.removeIf(a -> !a.getStatus().equals(status));
            }
        } else if (isDoctor(request)) {
            // 医生可以查看自己的预约，或者指定患者的预约
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null) {
                return Result.error("医生信息不存在");
            }
            
            if (patientId != null) {
                // 查询指定患者的预约
                appointments = appointmentService.getAppointmentsByPatientId(patientId);
                // 如果医生查询其他医生的患者预约，则只返回自己的预约
                appointments.removeIf(a -> !a.getDoctorId().equals(doctor.getDoctorId()));
            } else {
                // 查询医生自己的所有预约
                appointments = appointmentService.getAppointmentsByDoctorId(doctor.getDoctorId());
            }
            
            // 如果指定了状态，则在内存中过滤
            if (status != null && !status.isEmpty()) {
                appointments.removeIf(a -> !a.getStatus().equals(status));
            }
        } else if (isAdmin(request)) {
            // 管理员可以查看所有预约
            if (patientId != null) {
                // 查询指定患者的预约
                appointments = appointmentService.getAppointmentsByPatientId(patientId);
            } else if (doctorId != null) {
                // 查询指定医生的预约
                appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
            } else if (status != null && !status.isEmpty()) {
                // 查询指定状态的预约
                appointments = appointmentService.getAppointmentsByStatus(status);
            } else {
                // 查询所有预约（这里需要自定义查询方法，暂时返回错误信息）
                return Result.validateFailed("请指定查询条件");
            }
        } else {
            return Result.forbidden();
        }

        return Result.success(appointments);
    }

    /**
     * 获取预约详情
     * @param appointmentId 预约ID
     * @param request HTTP请求
     * @return 预约详情
     */
    @GetMapping("/{appointmentId}")
    public Result getAppointment(@PathVariable Integer appointmentId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询预约
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null) {
            return Result.error("预约不存在");
        }

        // 根据角色判断是否有权限查看
        if (isPatient(request)) {
            // 患者只能查看自己的预约
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || !patient.getPatientId().equals(appointment.getPatientId())) {
                return Result.forbidden();
            }
        } else if (isDoctor(request)) {
            // 医生只能查看自己的预约
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(appointment.getDoctorId())) {
                return Result.forbidden();
            }
        } else if (isAdmin(request)) {
            // 管理员可以查看所有预约
        } else {
            return Result.forbidden();
        }

        return Result.success(appointment);
    }

    /**
     * 添加预约
     * @param appointment 预约信息
     * @param request HTTP请求
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addAppointment(@RequestBody Appointment appointment, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 验证预约信息
        if (appointment.getDoctorId() == null) {
            return Result.validateFailed("请选择医生");
        }
        if (appointment.getAppointmentDate() == null) {
            return Result.validateFailed("请选择预约日期");
        }
        if (appointment.getTimeSlot() == null || appointment.getTimeSlot().isEmpty()) {
            return Result.validateFailed("请选择预约时间段");
        }
        if (appointment.getAppointmentType() == null || appointment.getAppointmentType().isEmpty()) {
            return Result.validateFailed("请选择预约类型");
        }

        // 根据角色设置患者ID
        if (isPatient(request)) {
            // 患者只能为自己预约
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                return Result.error("患者信息不存在");
            }
            appointment.setPatientId(patient.getPatientId());
        } else if (hasAnyRole(request, ROLE_DOCTOR, ROLE_ADMIN)) {
            // 医生和管理员可以为患者预约，需要指定患者ID
            if (appointment.getPatientId() == null) {
                return Result.validateFailed("请选择患者");
            }
        } else {
            return Result.forbidden();
        }

        // 添加预约
        boolean success = appointmentService.addAppointment(appointment);
        if (success) {
            return Result.success(appointment);
        } else {
            return Result.error("添加预约失败");
        }
    }

    /**
     * 更新预约
     * @param appointment 预约信息
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result updateAppointment(@RequestBody Appointment appointment, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 验证预约ID
        if (appointment.getAppointmentId() == null) {
            return Result.validateFailed("预约ID不能为空");
        }

        // 查询原预约信息
        Appointment originalAppointment = appointmentService.getAppointmentById(appointment.getAppointmentId());
        if (originalAppointment == null) {
            return Result.error("预约不存在");
        }

        // 根据角色判断是否有权限更新
        if (isPatient(request)) {
            // 患者只能更新自己的预约，且不能修改患者ID和医生ID
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || !patient.getPatientId().equals(originalAppointment.getPatientId())) {
                return Result.forbidden();
            }
            
            // 不允许修改患者ID和医生ID
            appointment.setPatientId(originalAppointment.getPatientId());
            appointment.setDoctorId(originalAppointment.getDoctorId());
            
            // 患者只能取消预约，不能修改其他状态
            if (appointment.getStatus() != null && !appointment.getStatus().equals("已取消")) {
                return Result.validateFailed("患者只能取消预约");
            }
        } else if (isDoctor(request)) {
            // 医生只能更新自己的预约，且不能修改患者ID和医生ID
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(originalAppointment.getDoctorId())) {
                return Result.forbidden();
            }
            
            // 不允许修改患者ID和医生ID
            appointment.setPatientId(originalAppointment.getPatientId());
            appointment.setDoctorId(originalAppointment.getDoctorId());
        } else if (isAdmin(request)) {
            // 管理员可以更新所有预约
        } else {
            return Result.forbidden();
        }

        // 更新预约
        boolean success = appointmentService.updateAppointment(appointment);
        if (success) {
            return Result.success(appointment);
        } else {
            return Result.error("更新预约失败");
        }
    }

    /**
     * 取消预约
     * @param appointmentId 预约ID
     * @param request HTTP请求
     * @return 取消结果
     */
    @PutMapping("/cancel/{appointmentId}")
    public Result cancelAppointment(@PathVariable Integer appointmentId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询预约
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null) {
            return Result.error("预约不存在");
        }

        // 根据角色判断是否有权限取消
        if (isPatient(request)) {
            // 患者只能取消自己的预约
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null || !patient.getPatientId().equals(appointment.getPatientId())) {
                return Result.forbidden();
            }
        } else if (isDoctor(request)) {
            // 医生只能取消自己的预约
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(appointment.getDoctorId())) {
                return Result.forbidden();
            }
        } else if (isAdmin(request)) {
            // 管理员可以取消所有预约
        } else {
            return Result.forbidden();
        }

        // 取消预约
        boolean success = appointmentService.cancelAppointment(appointmentId);
        if (success) {
            return Result.success(null);
        } else {
            return Result.error("取消预约失败");
        }
    }

    /**
     * 确认预约
     * @param appointmentId 预约ID
     * @param request HTTP请求
     * @return 确认结果
     */
    @PutMapping("/confirm/{appointmentId}")
    public Result confirmAppointment(@PathVariable Integer appointmentId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询预约
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null) {
            return Result.error("预约不存在");
        }

        // 只有医生和管理员可以确认预约
        if (isDoctor(request)) {
            // 医生只能确认自己的预约
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(appointment.getDoctorId())) {
                return Result.forbidden();
            }
        } else if (isAdmin(request)) {
            // 管理员可以确认所有预约
        } else {
            return Result.forbidden();
        }

        // 确认预约
        boolean success = appointmentService.confirmAppointment(appointmentId);
        if (success) {
            return Result.success(null);
        } else {
            return Result.error("确认预约失败");
        }
    }

    /**
     * 完成预约
     * @param appointmentId 预约ID
     * @param request HTTP请求
     * @return 完成结果
     */
    @PutMapping("/complete/{appointmentId}")
    public Result completeAppointment(@PathVariable Integer appointmentId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 查询预约
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null) {
            return Result.error("预约不存在");
        }

        // 只有医生和管理员可以完成预约
        if (isDoctor(request)) {
            // 医生只能完成自己的预约
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null || !doctor.getDoctorId().equals(appointment.getDoctorId())) {
                return Result.forbidden();
            }
        } else if (isAdmin(request)) {
            // 管理员可以完成所有预约
        } else {
            return Result.forbidden();
        }

        // 完成预约
        boolean success = appointmentService.completeAppointment(appointmentId);
        if (success) {
            return Result.success(null);
        } else {
            return Result.error("完成预约失败");
        }
    }

    /**
     * 查询医生某天的预约情况
     * @param doctorId 医生ID
     * @param date 日期
     * @param request HTTP请求
     * @return 预约列表
     */
    @GetMapping("/doctor/schedule")
    public Result getDoctorSchedule(
            @RequestParam Integer doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            HttpServletRequest request) {
        
        // 验证用户是否已登录
        if (getCurrentUser(request) == null) {
            return Result.unauthorized();
        }

        // 查询医生某天的预约
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorIdAndDate(doctorId, date);
        return Result.success(appointments);
    }
} 