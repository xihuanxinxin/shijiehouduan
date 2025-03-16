package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.AdminUser;
import com.example.shijiehouduan.service.AdminUserService;
import com.example.shijiehouduan.service.SystemLogService;
import com.example.shijiehouduan.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理员用户控制器
 */
@RestController
@RequestMapping("/api/system/admins")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;
    
    @Autowired
    private SystemLogService systemLogService;

    /**
     * 管理员登录
     * @param loginMap 登录信息
     * @param request HTTP请求
     * @return 结果
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> loginMap, HttpServletRequest request) {
        try {
            String username = loginMap.get("username");
            String password = loginMap.get("password");
            
            if (username == null || password == null) {
                return Result.error("用户名和密码不能为空");
            }
            
            AdminUser adminUser = adminUserService.login(username, password);
            if (adminUser != null) {
                // 检查用户状态
                if ("禁用".equals(adminUser.getStatus())) {
                    return Result.error("账号已被禁用，请联系管理员");
                }
                
                // 记录登录日志
                systemLogService.addLog(adminUser.getAdminId(), "登录", "管理员登录系统", getIpAddress(request));
                
                // 将用户信息存入session
                HttpSession session = request.getSession();
                session.setAttribute("adminUser", adminUser);
                
                // 隐藏密码
                adminUser.setPassword(null);
                
                return Result.success("登录成功", adminUser);
            } else {
                return Result.error("用户名或密码错误");
            }
        } catch (Exception e) {
            return Result.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 管理员退出登录
     * @param request HTTP请求
     * @return 结果
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            AdminUser adminUser = (AdminUser) session.getAttribute("adminUser");
            
            if (adminUser != null) {
                // 记录退出日志
                systemLogService.addLog(adminUser.getAdminId(), "退出", "管理员退出系统", getIpAddress(request));
                
                // 清除session
                session.removeAttribute("adminUser");
                session.invalidate();
            }
            
            return Result.success("退出成功");
        } catch (Exception e) {
            return Result.error("退出失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询管理员
     * @param adminId 管理员ID
     * @return 结果
     */
    @GetMapping("/{adminId}")
    public Result getAdminById(@PathVariable Integer adminId) {
        try {
            AdminUser adminUser = adminUserService.getAdminById(adminId);
            if (adminUser != null) {
                // 隐藏密码
                adminUser.setPassword(null);
                return Result.success(adminUser);
            } else {
                return Result.error("管理员不存在");
            }
        } catch (Exception e) {
            return Result.error("查询管理员失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return 结果
     */
    @GetMapping("/username/{username}")
    public Result getAdminByUsername(@PathVariable String username) {
        try {
            AdminUser adminUser = adminUserService.getAdminByUsername(username);
            if (adminUser != null) {
                // 隐藏密码
                adminUser.setPassword(null);
                return Result.success(adminUser);
            } else {
                return Result.error("管理员不存在");
            }
        } catch (Exception e) {
            return Result.error("查询管理员失败: " + e.getMessage());
        }
    }

    /**
     * 根据角色查询管理员
     * @param role 角色
     * @return 结果
     */
    @GetMapping("/role/{role}")
    public Result getAdminsByRole(@PathVariable String role) {
        try {
            List<AdminUser> adminUsers = adminUserService.getAdminsByRole(role);
            // 隐藏密码
            adminUsers.forEach(adminUser -> adminUser.setPassword(null));
            return Result.success(adminUsers);
        } catch (Exception e) {
            return Result.error("查询管理员失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态查询管理员
     * @param status 状态
     * @return 结果
     */
    @GetMapping("/status/{status}")
    public Result getAdminsByStatus(@PathVariable String status) {
        try {
            List<AdminUser> adminUsers = adminUserService.getAdminsByStatus(status);
            // 隐藏密码
            adminUsers.forEach(adminUser -> adminUser.setPassword(null));
            return Result.success(adminUsers);
        } catch (Exception e) {
            return Result.error("查询管理员失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有管理员
     * @return 结果
     */
    @GetMapping("/all")
    public Result getAllAdmins() {
        try {
            List<AdminUser> adminUsers = adminUserService.getAllAdmins();
            // 隐藏密码
            adminUsers.forEach(adminUser -> adminUser.setPassword(null));
            return Result.success(adminUsers);
        } catch (Exception e) {
            return Result.error("查询所有管理员失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询管理员
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return 结果
     */
    @GetMapping("/page")
    public Result getAdminsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            List<AdminUser> adminUsers = adminUserService.getAdminsByPage(pageNum, pageSize);
            // 隐藏密码
            adminUsers.forEach(adminUser -> adminUser.setPassword(null));
            
            long total = adminUserService.countAllAdmins();
            Map<String, Object> data = new HashMap<>();
            data.put("list", adminUsers);
            data.put("total", total);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("分页查询管理员失败: " + e.getMessage());
        }
    }

    /**
     * 添加管理员
     * @param adminUser 管理员用户
     * @param request HTTP请求
     * @return 结果
     */
    @PostMapping
    public Result addAdmin(@RequestBody AdminUser adminUser, HttpServletRequest request) {
        try {
            // 检查用户名是否存在
            if (adminUserService.checkUsernameExists(adminUser.getUsername())) {
                return Result.error("用户名已存在");
            }
            
            boolean success = adminUserService.addAdmin(adminUser);
            if (success) {
                // 记录操作日志
                HttpSession session = request.getSession();
                AdminUser currentAdmin = (AdminUser) session.getAttribute("adminUser");
                if (currentAdmin != null) {
                    systemLogService.addLog(currentAdmin.getAdminId(), "添加管理员", 
                            "添加管理员: " + adminUser.getUsername(), getIpAddress(request));
                }
                
                return Result.success("添加管理员成功");
            } else {
                return Result.error("添加管理员失败");
            }
        } catch (Exception e) {
            return Result.error("添加管理员失败: " + e.getMessage());
        }
    }

    /**
     * 更新管理员
     * @param adminUser 管理员用户
     * @param request HTTP请求
     * @return 结果
     */
    @PutMapping
    public Result updateAdmin(@RequestBody AdminUser adminUser, HttpServletRequest request) {
        try {
            boolean success = adminUserService.updateAdmin(adminUser);
            if (success) {
                // 记录操作日志
                HttpSession session = request.getSession();
                AdminUser currentAdmin = (AdminUser) session.getAttribute("adminUser");
                if (currentAdmin != null) {
                    systemLogService.addLog(currentAdmin.getAdminId(), "更新管理员", 
                            "更新管理员: " + adminUser.getUsername(), getIpAddress(request));
                }
                
                return Result.success("更新管理员成功");
            } else {
                return Result.error("更新管理员失败");
            }
        } catch (Exception e) {
            return Result.error("更新管理员失败: " + e.getMessage());
        }
    }

    /**
     * 更新管理员状态
     * @param adminId 管理员ID
     * @param status 状态
     * @param request HTTP请求
     * @return 结果
     */
    @PutMapping("/{adminId}/status")
    public Result updateAdminStatus(
            @PathVariable Integer adminId,
            @RequestParam String status,
            HttpServletRequest request) {
        try {
            boolean success = adminUserService.updateAdminStatus(adminId, status);
            if (success) {
                // 记录操作日志
                HttpSession session = request.getSession();
                AdminUser currentAdmin = (AdminUser) session.getAttribute("adminUser");
                if (currentAdmin != null) {
                    systemLogService.addLog(currentAdmin.getAdminId(), "更新管理员状态", 
                            "更新管理员状态: ID=" + adminId + ", 状态=" + status, getIpAddress(request));
                }
                
                return Result.success("更新管理员状态成功");
            } else {
                return Result.error("更新管理员状态失败");
            }
        } catch (Exception e) {
            return Result.error("更新管理员状态失败: " + e.getMessage());
        }
    }

    /**
     * 更新管理员密码
     * @param passwordMap 密码信息
     * @param request HTTP请求
     * @return 结果
     */
    @PutMapping("/password")
    public Result updateAdminPassword(@RequestBody Map<String, String> passwordMap, HttpServletRequest request) {
        try {
            Integer adminId = Integer.parseInt(passwordMap.get("adminId"));
            String oldPassword = passwordMap.get("oldPassword");
            String newPassword = passwordMap.get("newPassword");
            
            boolean success = adminUserService.updateAdminPassword(adminId, oldPassword, newPassword);
            if (success) {
                // 记录操作日志
                HttpSession session = request.getSession();
                AdminUser currentAdmin = (AdminUser) session.getAttribute("adminUser");
                if (currentAdmin != null) {
                    systemLogService.addLog(currentAdmin.getAdminId(), "更新管理员密码", 
                            "更新管理员密码: ID=" + adminId, getIpAddress(request));
                }
                
                return Result.success("更新密码成功");
            } else {
                return Result.error("更新密码失败，旧密码可能不正确");
            }
        } catch (Exception e) {
            return Result.error("更新密码失败: " + e.getMessage());
        }
    }

    /**
     * 重置管理员密码
     * @param adminId 管理员ID
     * @param request HTTP请求
     * @return 结果
     */
    @PutMapping("/{adminId}/reset-password")
    public Result resetAdminPassword(@PathVariable Integer adminId, HttpServletRequest request) {
        try {
            String newPassword = adminUserService.resetAdminPassword(adminId);
            if (newPassword != null) {
                // 记录操作日志
                HttpSession session = request.getSession();
                AdminUser currentAdmin = (AdminUser) session.getAttribute("adminUser");
                if (currentAdmin != null) {
                    systemLogService.addLog(currentAdmin.getAdminId(), "重置管理员密码", 
                            "重置管理员密码: ID=" + adminId, getIpAddress(request));
                }
                
                Map<String, String> data = new HashMap<>();
                data.put("newPassword", newPassword);
                return Result.success("重置密码成功", data);
            } else {
                return Result.error("重置密码失败");
            }
        } catch (Exception e) {
            return Result.error("重置密码失败: " + e.getMessage());
        }
    }

    /**
     * 删除管理员
     * @param adminId 管理员ID
     * @param request HTTP请求
     * @return 结果
     */
    @DeleteMapping("/{adminId}")
    public Result deleteAdmin(@PathVariable Integer adminId, HttpServletRequest request) {
        try {
            boolean success = adminUserService.deleteAdmin(adminId);
            if (success) {
                // 记录操作日志
                HttpSession session = request.getSession();
                AdminUser currentAdmin = (AdminUser) session.getAttribute("adminUser");
                if (currentAdmin != null) {
                    systemLogService.addLog(currentAdmin.getAdminId(), "删除管理员", 
                            "删除管理员: ID=" + adminId, getIpAddress(request));
                }
                
                return Result.success("删除管理员成功");
            } else {
                return Result.error("删除管理员失败");
            }
        } catch (Exception e) {
            return Result.error("删除管理员失败: " + e.getMessage());
        }
    }

    /**
     * 统计管理员总数
     * @return 结果
     */
    @GetMapping("/count")
    public Result countAllAdmins() {
        try {
            long count = adminUserService.countAllAdmins();
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计管理员失败: " + e.getMessage());
        }
    }

    /**
     * 统计各角色的管理员数量
     * @return 结果
     */
    @GetMapping("/count/role")
    public Result countAdminsByRole() {
        try {
            List<Map<String, Object>> data = adminUserService.countAdminsByRole();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计角色管理员失败: " + e.getMessage());
        }
    }

    /**
     * 统计各状态的管理员数量
     * @return 结果
     */
    @GetMapping("/count/status")
    public Result countAdminsByStatus() {
        try {
            List<Map<String, Object>> data = adminUserService.countAdminsByStatus();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计状态管理员失败: " + e.getMessage());
        }
    }

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 结果
     */
    @GetMapping("/check-username")
    public Result checkUsernameExists(@RequestParam String username) {
        try {
            boolean exists = adminUserService.checkUsernameExists(username);
            Map<String, Boolean> data = new HashMap<>();
            data.put("exists", exists);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("检查用户名失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取客户端IP地址
     * @param request HTTP请求
     * @return IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 