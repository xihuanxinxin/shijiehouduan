package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.AdminUser;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统管理员用户服务接口
 */
public interface AdminUserService {
    
    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码
     * @return 管理员用户
     */
    AdminUser login(String username, String password);
    
    /**
     * 根据ID查询管理员
     * @param adminId 管理员ID
     * @return 管理员用户
     */
    AdminUser getAdminById(Integer adminId);
    
    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return 管理员用户
     */
    AdminUser getAdminByUsername(String username);
    
    /**
     * 根据角色查询管理员
     * @param role 角色
     * @return 管理员用户列表
     */
    List<AdminUser> getAdminsByRole(String role);
    
    /**
     * 根据状态查询管理员
     * @param status 状态
     * @return 管理员用户列表
     */
    List<AdminUser> getAdminsByStatus(String status);
    
    /**
     * 查询所有管理员
     * @return 管理员用户列表
     */
    List<AdminUser> getAllAdmins();
    
    /**
     * 分页查询管理员
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return 管理员用户列表
     */
    List<AdminUser> getAdminsByPage(Integer pageNum, Integer pageSize);
    
    /**
     * 添加管理员
     * @param adminUser 管理员用户
     * @return 是否成功
     */
    boolean addAdmin(AdminUser adminUser);
    
    /**
     * 更新管理员
     * @param adminUser 管理员用户
     * @return 是否成功
     */
    boolean updateAdmin(AdminUser adminUser);
    
    /**
     * 更新管理员状态
     * @param adminId 管理员ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateAdminStatus(Integer adminId, String status);
    
    /**
     * 更新管理员密码
     * @param adminId 管理员ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updateAdminPassword(Integer adminId, String oldPassword, String newPassword);
    
    /**
     * 重置管理员密码
     * @param adminId 管理员ID
     * @return 新密码
     */
    String resetAdminPassword(Integer adminId);
    
    /**
     * 更新管理员最后登录时间
     * @param adminId 管理员ID
     * @param lastLogin 最后登录时间
     * @return 是否成功
     */
    boolean updateAdminLastLogin(Integer adminId, Date lastLogin);
    
    /**
     * 删除管理员
     * @param adminId 管理员ID
     * @return 是否成功
     */
    boolean deleteAdmin(Integer adminId);
    
    /**
     * 统计管理员总数
     * @return 管理员总数
     */
    long countAllAdmins();
    
    /**
     * 统计各角色的管理员数量
     * @return 统计结果
     */
    List<Map<String, Object>> countAdminsByRole();
    
    /**
     * 统计各状态的管理员数量
     * @return 统计结果
     */
    List<Map<String, Object>> countAdminsByStatus();
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);
} 