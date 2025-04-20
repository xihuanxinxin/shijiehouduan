package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.dto.LoginRequest;
import com.example.shijiehouduan.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/auth")
public class LoginController extends BaseController {

    /**
     * 用户登录 - 表单提交方式
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
    public Result loginForm(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> loginResult = userService.login(username, password);
        
        if ((Boolean) loginResult.get("success")) {
            // 登录成功，生成token
            User user = (User) loginResult.get("user");
            
            // 生成JWT Token
            String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRoleType());
            loginResult.put("token", token);
            
            // 隐藏密码
            user.setPassword(null);
            
            return Result.success(loginResult);
        } else {
            return Result.error(loginResult.get("message").toString());
        }
    }
    
    /**
     * 用户登录 - JSON提交方式
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public Result loginJson(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> loginResult = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        
        if ((Boolean) loginResult.get("success")) {
            // 登录成功，生成token
            User user = (User) loginResult.get("user");
            
            // 生成JWT Token
            String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRoleType());
            loginResult.put("token", token);
            
            // 隐藏密码
            user.setPassword(null);
            
            return Result.success(loginResult);
        } else {
            return Result.error(loginResult.get("message").toString());
        }
    }
    
    /**
     * 用户退出登录
     * @return 退出结果
     */
    @PostMapping("/logout")
    public Result logout() {
        // 注意：由于使用token认证，服务端无法直接使token失效
        // 客户端需要自行清除token
        return Result.success("退出登录成功");
    }
    
    /**
     * 获取当前用户信息 - 使用Token认证
     * @param request HTTP请求
     * @return 当前用户信息
     */
    @GetMapping("/info")
    public Result getUserInfo(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 隐藏密码
        currentUser.setPassword(null);
        
        // 构造返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("user", currentUser);
        data.put("userId", currentUser.getUserId());
        data.put("roleType", currentUser.getRoleType());
        
        return Result.success(data);
    }
    
    /**
     * 刷新token
     * @param request HTTP请求
     * @return 新的token
     */
    @PostMapping("/refresh-token")
    public Result refreshToken(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 生成新的JWT Token
        String newToken = jwtUtil.generateToken(currentUser.getUserId(), currentUser.getUsername(), currentUser.getRoleType());
        
        // 构造返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", newToken);
        data.put("userId", currentUser.getUserId());
        
        return Result.success("Token刷新成功", data);
    }
    
    /**
     * 验证用户角色
     * @param request HTTP请求
     * @return 角色验证结果
     */
    @GetMapping("/check-role")
    public Result checkRole(HttpServletRequest request, @RequestParam String role) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        boolean hasRole = false;
        if (ROLE_PATIENT.equals(role) && isPatient(request)) {
            hasRole = true;
        } else if (ROLE_DOCTOR.equals(role) && isDoctor(request)) {
            hasRole = true;
        } else if (ROLE_ADMIN.equals(role) && isAdmin(request)) {
            hasRole = true;
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("hasRole", hasRole);
        data.put("currentRole", currentUser.getRoleType());
        data.put("requestedRole", role);
        
        return Result.success(data);
    }
} 