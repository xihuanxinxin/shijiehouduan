package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生控制器
 */
@RestController
@RequestMapping("/api/doctor")
public class DoctorController extends BaseController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    /**
     * 获取医生列表
     * @param specialty 专科（可选）
     * @param title 职称（可选）
     * @return 医生列表
     */
    @GetMapping("/list")
    public Result getDoctors(
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String title) {
        
        List<Doctor> doctors;
        
        if (specialty != null && !specialty.isEmpty()) {
            // 根据专科查询
            doctors = doctorService.getDoctorsBySpecialty(specialty);
        } else if (title != null && !title.isEmpty()) {
            // 根据职称查询
            doctors = doctorService.getDoctorsByTitle(title);
        } else {
            // 查询所有医生
            doctors = doctorService.getAllDoctors();
        }
        
        return Result.success(doctors);
    }

    /**
     * 获取医生详情
     * @param doctorId 医生ID
     * @return 医生详情
     */
    @GetMapping("/{doctorId}")
    public Result getDoctor(@PathVariable Integer doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        if (doctor == null) {
            return Result.error("医生不存在");
        }
        return Result.success(doctor);
    }

    /**
     * 获取当前登录医生的信息
     * @param request HTTP请求
     * @return 医生信息
     */
    @GetMapping("/info")
    public Result getDoctorInfo(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有医生可以访问此接口
        if (!isDoctor(request)) {
            return Result.forbidden();
        }
        
        // 查询医生信息
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
        if (doctor == null) {
            return Result.error("医生信息不存在");
        }
        
        return Result.success(doctor);
    }

    /**
     * 更新医生信息
     * @param doctor 医生信息
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result updateDoctor(@RequestBody Doctor doctor, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 验证权限
        if (isDoctor(request)) {
            // 医生只能更新自己的信息
            Doctor existingDoctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (existingDoctor == null) {
                return Result.error("医生信息不存在");
            }
            
            if (!existingDoctor.getDoctorId().equals(doctor.getDoctorId())) {
                return Result.forbidden();
            }
            
            // 不允许修改用户ID
            doctor.setUserId(existingDoctor.getUserId());
        } else if (isAdmin(request)) {
            // 管理员可以更新所有医生信息
            Doctor existingDoctor = doctorService.getDoctorById(doctor.getDoctorId());
            if (existingDoctor == null) {
                return Result.error("医生不存在");
            }
        } else {
            return Result.forbidden();
        }
        
        // 更新医生信息
        boolean success = doctorService.updateDoctor(doctor);
        if (success) {
            return Result.success(doctor);
        } else {
            return Result.error("更新医生信息失败");
        }
    }

    /**
     * 更新医生排班
     * @param doctorId 医生ID
     * @param schedule 排班信息
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/{doctorId}/schedule")
    public Result updateDoctorSchedule(
            @PathVariable Integer doctorId,
            @RequestBody String schedule,
            HttpServletRequest request) {
        
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 验证权限
        if (isDoctor(request)) {
            // 医生只能更新自己的排班
            Doctor existingDoctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (existingDoctor == null) {
                return Result.error("医生信息不存在");
            }
            
            if (!existingDoctor.getDoctorId().equals(doctorId)) {
                return Result.forbidden();
            }
        } else if (isAdmin(request)) {
            // 管理员可以更新所有医生排班
            Doctor existingDoctor = doctorService.getDoctorById(doctorId);
            if (existingDoctor == null) {
                return Result.error("医生不存在");
            }
        } else {
            return Result.forbidden();
        }
        
        // 更新医生排班
        boolean success = doctorService.updateDoctorSchedule(doctorId, schedule);
        if (success) {
            Map<String, Object> data = new HashMap<>();
            data.put("doctorId", doctorId);
            data.put("schedule", schedule);
            return Result.success(data);
        } else {
            return Result.error("更新医生排班失败");
        }
    }

    /**
     * 添加医生（仅管理员）
     * @param doctor 医生信息
     * @param request HTTP请求
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addDoctor(@RequestBody Doctor doctor, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有管理员可以添加医生
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        // 验证医生信息
        if (doctor.getUserId() == null) {
            return Result.validateFailed("用户ID不能为空");
        }
        
        // 检查用户是否存在
        User user = userService.getUserById(doctor.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 检查用户角色是否为医生
        if (!ROLE_DOCTOR.equals(user.getRoleType())) {
            return Result.validateFailed("只能为医生角色的用户添加医生信息");
        }
        
        // 检查医生信息是否已存在
        Doctor existingDoctor = doctorService.getDoctorByUserId(doctor.getUserId());
        if (existingDoctor != null) {
            return Result.error("该用户的医生信息已存在");
        }
        
        // 添加医生信息
        boolean success = doctorService.addDoctor(doctor);
        if (success) {
            return Result.success(doctor);
        } else {
            return Result.error("添加医生信息失败");
        }
    }
} 