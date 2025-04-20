package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Bed;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 床位控制器
 */
@RestController
@RequestMapping("/api/bed")
public class BedController extends BaseController {

    @Autowired
    private BedService bedService;

    /**
     * 获取所有床位
     * @param status 床位状态（可选）
     * @param roomNumber 房间号（可选）
     * @param request HTTP请求
     * @return 床位列表
     */
    @GetMapping("/list")
    public Result getAllBeds(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String roomNumber,
            HttpServletRequest request) {
        
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以查看床位信息
        if (!hasAnyRole(request, ROLE_DOCTOR, ROLE_ADMIN)) {
            return Result.forbidden();
        }

        // 根据参数查询床位
        List<Bed> beds;
        if (status != null && !status.isEmpty()) {
            beds = bedService.getBedsByStatus(status);
            
            // 如果同时指定了房间号，则在内存中过滤
            if (roomNumber != null && !roomNumber.isEmpty()) {
                beds.removeIf(b -> !b.getRoomNumber().equals(roomNumber));
            }
        } else if (roomNumber != null && !roomNumber.isEmpty()) {
            beds = bedService.getBedsByRoomNumber(roomNumber);
        } else {
            beds = bedService.getAllBeds();
        }

        return Result.success(beds);
    }

    /**
     * 获取床位详情
     * @param bedId 床位ID
     * @param request HTTP请求
     * @return 床位详情
     */
    @GetMapping("/{bedId}")
    public Result getBed(@PathVariable Integer bedId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以查看床位信息
        if (!hasAnyRole(request, ROLE_DOCTOR, ROLE_ADMIN)) {
            return Result.forbidden();
        }

        // 查询床位
        Bed bed = bedService.getBedById(bedId);
        if (bed == null) {
            return Result.error("床位不存在");
        }

        return Result.success(bed);
    }

    /**
     * 获取空闲床位
     * @param request HTTP请求
     * @return 空闲床位列表
     */
    @GetMapping("/available")
    public Result getAvailableBeds(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以查看床位信息
        if (!hasAnyRole(request, ROLE_DOCTOR, ROLE_ADMIN)) {
            return Result.forbidden();
        }

        // 查询空闲床位
        List<Bed> beds = bedService.getBedsByStatus("空闲");
        return Result.success(beds);
    }

    /**
     * 获取占用床位
     * @param request HTTP请求
     * @return 占用床位列表
     */
    @GetMapping("/occupied")
    public Result getOccupiedBeds(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以查看床位信息
        if (!hasAnyRole(request, ROLE_DOCTOR, ROLE_ADMIN)) {
            return Result.forbidden();
        }

        // 查询占用床位
        List<Bed> beds = bedService.getBedsByStatus("占用");
        return Result.success(beds);
    }

    /**
     * 获取维修中床位
     * @param request HTTP请求
     * @return 维修中床位列表
     */
    @GetMapping("/maintenance")
    public Result getMaintenanceBeds(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以查看床位信息
        if (!hasAnyRole(request, ROLE_DOCTOR, ROLE_ADMIN)) {
            return Result.forbidden();
        }

        // 查询维修中床位
        List<Bed> beds = bedService.getBedsByStatus("维修");
        return Result.success(beds);
    }

    /**
     * 获取患者当前床位
     * @param patientId 患者ID
     * @param request HTTP请求
     * @return 患者当前床位
     */
    @GetMapping("/patient/{patientId}")
    public Result getPatientBed(@PathVariable Integer patientId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }

        // 只有医生和管理员可以查看床位信息
        if (!hasAnyRole(request, ROLE_DOCTOR, ROLE_ADMIN)) {
            return Result.forbidden();
        }

        // 查询患者当前床位
        Bed bed = bedService.getBedByCurrentPatientId(patientId);
        if (bed == null) {
            return Result.error("患者当前没有床位");
        }

        return Result.success(bed);
    }
} 