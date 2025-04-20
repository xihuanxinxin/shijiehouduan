package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 */
@RestController
public class HomeController extends BaseController {

    /**
     * 首页
     */
    @GetMapping("/")
    public Result home() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "欢迎访问世界后端API");
        data.put("timestamp", System.currentTimeMillis());
        data.put("status", "running");
        
        return Result.success("API服务正常运行", data);
    }
    
    /**
     * 测试根路径
     */
    @GetMapping("/shijiehouduan_war_exploded")
    public Result root() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "欢迎访问世界后端API");
        data.put("timestamp", System.currentTimeMillis());
        data.put("status", "running");
        data.put("availableApis", new String[]{
            "/shijiehouduan_war_exploded/test/ping",
            "/shijiehouduan_war_exploded/test/db"
        });
        
        return Result.success("API服务正常运行", data);
    }
    
    /**
     * 用户信息接口 - 需要token认证
     */
    @GetMapping("/userinfo")
    public Result getUserInfo(HttpServletRequest request) {
        // 获取当前登录用户
        com.example.shijiehouduan.entity.User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", currentUser.getUserId());
        data.put("username", currentUser.getUsername());
        data.put("roleType", currentUser.getRoleType());
        
        return Result.success("获取用户信息成功", data);
    }
    
    /**
     * 管理员专用接口
     */
    @GetMapping("/admin/info")
    public Result getAdminInfo(HttpServletRequest request) {
        // 验证是否是管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("systemStatus", "正常");
        data.put("timestamp", System.currentTimeMillis());
        data.put("role", "管理员专用接口");
        
        return Result.success("获取管理员信息成功", data);
    }
} 