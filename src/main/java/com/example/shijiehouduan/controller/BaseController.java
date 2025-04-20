package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.UserService;
import com.example.shijiehouduan.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 基础控制器，提供通用方法
 */
public class BaseController {

    @Autowired
    protected UserService userService;
    
    @Autowired
    protected JwtUtil jwtUtil;
    
    // 系统中的角色类型
    protected static final String ROLE_PATIENT = "患者";
    protected static final String ROLE_DOCTOR = "医生";
    protected static final String ROLE_ADMIN = "管理员";
    
    // 所有有效的角色类型列表
    protected static final List<String> VALID_ROLES = Arrays.asList(ROLE_PATIENT, ROLE_DOCTOR, ROLE_ADMIN);
    
    /**
     * 从请求头获取当前用户
     * @param request HTTP请求
     * @return 当前用户，如果未认证则返回null
     */
    protected User getCurrentUser(HttpServletRequest request) {
        // 从请求头获取Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 解析token
            String token = authHeader.substring(7);
            try {
                Integer userId = jwtUtil.getUserIdFromToken(token);
                if (userId != null && jwtUtil.validateToken(token)) {
                    return userService.getUserById(userId);
                }
            } catch (Exception e) {
                // token解析失败
            }
        }
        
        // 兼容旧代码，也从session中尝试获取用户
        User sessionUser = (User) request.getSession().getAttribute("user");
        return sessionUser;
    }
    
    /**
     * 获取当前用户的角色类型
     * @param request HTTP请求
     * @return 角色类型，如果未认证则返回null
     */
    protected String getCurrentUserRole(HttpServletRequest request) {
        User currentUser = getCurrentUser(request);
        return currentUser != null ? currentUser.getRoleType() : null;
    }
    
    /**
     * 检查当前用户是否具有特定角色
     * @param request HTTP请求
     * @param roleType 需要检查的角色类型
     * @return 如果用户具有指定角色则返回true，否则返回false
     */
    protected boolean hasRole(HttpServletRequest request, String roleType) {
        String currentRole = getCurrentUserRole(request);
        return roleType != null && roleType.equals(currentRole);
    }
    
    /**
     * 检查当前用户是否具有特定角色之一
     * @param request HTTP请求
     * @param roleTypes 需要检查的角色类型数组
     * @return 如果用户具有指定角色之一则返回true，否则返回false
     */
    protected boolean hasAnyRole(HttpServletRequest request, String... roleTypes) {
        String currentRole = getCurrentUserRole(request);
        if (currentRole == null || roleTypes == null) {
            return false;
        }
        
        for (String role : roleTypes) {
            if (currentRole.equals(role)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 检查当前用户是否是管理员
     * @param request HTTP请求
     * @return 如果用户是管理员则返回true，否则返回false
     */
    protected boolean isAdmin(HttpServletRequest request) {
        return hasRole(request, ROLE_ADMIN);
    }
    
    /**
     * 检查当前用户是否是医生
     * @param request HTTP请求
     * @return 如果用户是医生则返回true，否则返回false
     */
    protected boolean isDoctor(HttpServletRequest request) {
        return hasRole(request, ROLE_DOCTOR);
    }
    
    /**
     * 检查当前用户是否是患者
     * @param request HTTP请求
     * @return 如果用户是患者则返回true，否则返回false
     */
    protected boolean isPatient(HttpServletRequest request) {
        return hasRole(request, ROLE_PATIENT);
    }
} 