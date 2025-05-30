package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.entity.Bed;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.BedService;
import com.example.shijiehouduan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 医疗资源管理控制器
 */
@RestController
@RequestMapping("/api/admin/resources")
public class MedicalResourceController extends BaseController {

    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private BedService bedService;

    @Autowired
    private UserService userService;

    /**
     * 获取所有医生列表
     */
    @GetMapping("/doctors")
    public Result getAllDoctors(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            return Result.success(doctors);
        } catch (Exception e) {
            return Result.error("获取医生列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取医生
     */
    @GetMapping("/doctors/{doctorId}")
    public Result getDoctorById(@PathVariable Integer doctorId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            Doctor doctor = doctorService.getDoctorById(doctorId);
            if (doctor != null) {
                return Result.success(doctor);
            } else {
                return Result.error("医生不存在");
            }
        } catch (Exception e) {
            return Result.error("获取医生失败: " + e.getMessage());
        }
    }

    /**
     * 根据专业查询医生
     */
    @GetMapping("/doctors/specialty")
    public Result getDoctorsBySpecialty(@RequestParam String specialty, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Doctor> doctors = doctorService.getDoctorsBySpecialty(specialty);
            return Result.success(doctors);
        } catch (Exception e) {
            return Result.error("获取医生列表失败: " + e.getMessage());
        }
    }

    /**
     * 添加医生
     */
    @PostMapping("/doctors")
    public Result addDoctor(@RequestBody Doctor doctor, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            // 判断是创建新用户+医生，还是只创建医生
            if (doctor.getUserId() == null && doctor.getUser() != null) {
                // 需要先创建用户，再创建医生
                User user = doctor.getUser();
                
                // 设置用户角色为医生
                user.setRoleType(ROLE_DOCTOR);
                
                // 设置默认状态为启用（如果未提供）
                if (user.getStatus() == null) {
                    user.setStatus("启用");
                }
                
                // 设置创建时间
                user.setCreatedAt(new Date());
                
                // 创建用户
                boolean userResult = userService.addUser(user);
                if (!userResult) {
                    return Result.error("创建用户失败，可能用户名已存在");
                }
                
                // 设置医生的用户ID为新创建的用户ID
                doctor.setUserId(user.getUserId());
            } else if (doctor.getUserId() == null) {
                // 既没有提供userId，也没有提供user对象
                return Result.validateFailed("必须提供userId或user信息");
            } else {
                // 验证用户是否存在
                User existingUser = userService.getUserById(doctor.getUserId());
                if (existingUser == null) {
                    return Result.error("指定的用户ID不存在");
                }
                
                // 验证用户角色是否为医生
                if (!ROLE_DOCTOR.equals(existingUser.getRoleType())) {
                    // 自动更新用户角色为医生
                    existingUser.setRoleType(ROLE_DOCTOR);
                    userService.updateUser(existingUser);
                }
            }
            
            // 设置创建时间
            doctor.setCreatedAt(new Date());
            
            // 创建医生信息
            boolean result = doctorService.addDoctor(doctor);
            if (result) {
                return Result.success("添加医生成功");
            } else {
                return Result.error("添加医生失败");
            }
        } catch (Exception e) {
            return Result.error("添加医生失败: " + e.getMessage());
        }
    }

    /**
     * 更新医生
     */
    @PutMapping("/doctors/{doctorId}")
    public Result updateDoctor(@PathVariable Integer doctorId, @RequestBody Doctor doctor, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            // 设置医生ID
            doctor.setDoctorId(doctorId);
            
            boolean result = doctorService.updateDoctor(doctor);
            if (result) {
                return Result.success("更新医生成功");
            } else {
                return Result.error("更新医生失败，医生不存在");
            }
        } catch (Exception e) {
            return Result.error("更新医生失败: " + e.getMessage());
        }
    }

    /**
     * 删除医生
     */
    @DeleteMapping("/doctors/{doctorId}")
    public Result deleteDoctor(@PathVariable Integer doctorId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            boolean result = doctorService.deleteDoctor(doctorId);
            if (result) {
                return Result.success("删除医生成功");
            } else {
                return Result.error("删除医生失败，医生不存在");
            }
        } catch (Exception e) {
            return Result.error("删除医生失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有床位列表
     */
    @GetMapping("/beds")
    public Result getAllBeds(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Bed> beds = bedService.getAllBeds();
            return Result.success(beds);
        } catch (Exception e) {
            return Result.error("获取床位列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取床位
     */
    @GetMapping("/beds/{bedId}")
    public Result getBedById(@PathVariable Integer bedId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            Bed bed = bedService.getBedById(bedId);
            if (bed != null) {
                return Result.success(bed);
            } else {
                return Result.error("床位不存在");
            }
        } catch (Exception e) {
            return Result.error("获取床位失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态查询床位
     */
    @GetMapping("/beds/status")
    public Result getBedsByStatus(@RequestParam String status, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Bed> beds = bedService.getBedsByStatus(status);
            return Result.success(beds);
        } catch (Exception e) {
            return Result.error("获取床位列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据房间号查询床位
     */
    @GetMapping("/beds/room")
    public Result getBedsByRoomNumber(@RequestParam String roomNumber, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Bed> beds = bedService.getBedsByRoomNumber(roomNumber);
            return Result.success(beds);
        } catch (Exception e) {
            return Result.error("获取床位列表失败: " + e.getMessage());
        }
    }

    /**
     * 添加床位
     */
    @PostMapping("/beds")
    public Result addBed(@RequestBody Bed bed, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            // 设置默认状态为空闲
            if (bed.getStatus() == null) {
                bed.setStatus("空闲");
            }
            
            boolean result = bedService.addBed(bed);
            if (result) {
                return Result.success("添加床位成功");
            } else {
                return Result.error("添加床位失败");
            }
        } catch (Exception e) {
            return Result.error("添加床位失败: " + e.getMessage());
        }
    }

    /**
     * 更新床位
     */
    @PutMapping("/beds/{bedId}")
    public Result updateBed(@PathVariable Integer bedId, @RequestBody Bed bed, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            // 设置床位ID
            bed.setBedId(bedId);
            
            boolean result = bedService.updateBed(bed);
            if (result) {
                return Result.success("更新床位成功");
            } else {
                return Result.error("更新床位失败，床位不存在");
            }
        } catch (Exception e) {
            return Result.error("更新床位失败: " + e.getMessage());
        }
    }

    /**
     * 更新床位状态
     */
    @PutMapping("/beds/{bedId}/status")
    public Result updateBedStatus(@PathVariable Integer bedId, @RequestParam String status, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            boolean result = bedService.updateBedStatus(bedId, status);
            if (result) {
                return Result.success("更新床位状态成功");
            } else {
                return Result.error("更新床位状态失败，床位不存在");
            }
        } catch (Exception e) {
            return Result.error("更新床位状态失败: " + e.getMessage());
        }
    }

    /**
     * 删除床位
     */
    @DeleteMapping("/beds/{bedId}")
    public Result deleteBed(@PathVariable Integer bedId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            boolean result = bedService.deleteBed(bedId);
            if (result) {
                return Result.success("删除床位成功");
            } else {
                return Result.error("删除床位失败，床位不存在或正在使用中");
            }
        } catch (Exception e) {
            return Result.error("删除床位失败: " + e.getMessage());
        }
    }
} 