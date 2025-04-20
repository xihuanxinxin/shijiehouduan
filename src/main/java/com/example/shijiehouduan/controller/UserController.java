package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     * @param roleType 角色类型（可选）
     * @param status 状态（可选）
     * @param request HTTP请求
     * @return 用户列表
     */
    @GetMapping("/list")
    public Result getUserList(
            @RequestParam(required = false) String roleType,
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有管理员可以查看用户列表
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        List<User> users;
        
        if (roleType != null && !roleType.isEmpty()) {
            // 根据角色类型查询
            users = userService.getUsersByRoleType(roleType);
        } else if (status != null && !status.isEmpty()) {
            // 根据状态查询
            users = userService.getUsersByStatus(status);
        } else {
            // 查询所有用户
            users = userService.getAllUsers();
        }
        
        // 隐藏密码
        users.forEach(user -> user.setPassword(null));
        
        return Result.success(users);
    }
    
    /**
     * 分页获取用户列表
     * @param roleType 角色类型（可选）
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @param request HTTP请求
     * @return 用户列表
     */
    @GetMapping("/page")
    public Result getUserListByPage(
            @RequestParam(required = false) String roleType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有管理员可以查看用户列表
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        List<User> users;
        
        if (roleType != null && !roleType.isEmpty()) {
            // 根据角色类型分页查询
            users = userService.getUsersByRoleTypePage(roleType, pageNum, pageSize);
        } else {
            // 查询所有用户（暂不支持分页）
            users = userService.getAllUsers();
        }
        
        // 隐藏密码
        users.forEach(user -> user.setPassword(null));
        
        // 构造分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", users);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        
        return Result.success(result);
    }
    
    /**
     * 获取用户详情
     * @param userId 用户ID
     * @param request HTTP请求
     * @return 用户详情
     */
    @GetMapping("/{userId}")
    public Result getUserById(@PathVariable Integer userId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有管理员或者用户本人可以查看用户详情
        if (!isAdmin(request) && !currentUser.getUserId().equals(userId)) {
            return Result.forbidden();
        }
        
        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 隐藏密码
        user.setPassword(null);
        
        return Result.success(user);
    }
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping
    public Result updateUser(@RequestBody User user, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有管理员或者用户本人可以更新用户信息
        if (!isAdmin(request) && !currentUser.getUserId().equals(user.getUserId())) {
            return Result.forbidden();
        }
        
        // 更新用户信息
        boolean updated = userService.updateUser(user);
        if (updated) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 状态
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/{userId}/status")
    public Result updateUserStatus(
            @PathVariable Integer userId,
            @RequestParam String status,
            HttpServletRequest request) {
        
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有管理员可以更新用户状态
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        // 更新用户状态
        boolean updated = userService.updateUserStatus(userId, status);
        if (updated) {
            return Result.success("更新状态成功");
        } else {
            return Result.error("更新状态失败");
        }
    }
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @param request HTTP请求
     * @return 删除结果
     */
    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只有管理员可以删除用户
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        // 不能删除自己
        if (currentUser.getUserId().equals(userId)) {
            return Result.error("不能删除当前登录用户");
        }
        
        // 删除用户
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 