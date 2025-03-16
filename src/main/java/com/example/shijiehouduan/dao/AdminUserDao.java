package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统管理员用户数据访问接口
 */
@Mapper
public interface AdminUserDao {
    
    /**
     * 根据ID查询管理员
     * @param adminId 管理员ID
     * @return 管理员用户
     */
    AdminUser findById(Integer adminId);
    
    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return 管理员用户
     */
    AdminUser findByUsername(String username);
    
    /**
     * 根据角色查询管理员
     * @param role 角色
     * @return 管理员用户列表
     */
    List<AdminUser> findByRole(String role);
    
    /**
     * 根据状态查询管理员
     * @param status 状态
     * @return 管理员用户列表
     */
    List<AdminUser> findByStatus(String status);
    
    /**
     * 查询所有管理员
     * @return 管理员用户列表
     */
    List<AdminUser> findAll();
    
    /**
     * 分页查询管理员
     * @param offset 偏移量
     * @param limit 每页记录数
     * @return 管理员用户列表
     */
    List<AdminUser> findByPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 新增管理员
     * @param adminUser 管理员用户
     * @return 影响行数
     */
    int insert(AdminUser adminUser);
    
    /**
     * 更新管理员
     * @param adminUser 管理员用户
     * @return 影响行数
     */
    int update(AdminUser adminUser);
    
    /**
     * 更新管理员状态
     * @param adminId 管理员ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("adminId") Integer adminId, @Param("status") String status);
    
    /**
     * 更新管理员密码
     * @param adminId 管理员ID
     * @param password 密码
     * @return 影响行数
     */
    int updatePassword(@Param("adminId") Integer adminId, @Param("password") String password);
    
    /**
     * 更新管理员最后登录时间
     * @param adminId 管理员ID
     * @param lastLogin 最后登录时间
     * @return 影响行数
     */
    int updateLastLogin(@Param("adminId") Integer adminId, @Param("lastLogin") Date lastLogin);
    
    /**
     * 删除管理员
     * @param adminId 管理员ID
     * @return 影响行数
     */
    int delete(Integer adminId);
    
    /**
     * 统计管理员总数
     * @return 管理员总数
     */
    long countAll();
    
    /**
     * 统计各角色的管理员数量
     * @return 统计结果
     */
    List<Map<String, Object>> countByRole();
    
    /**
     * 统计各状态的管理员数量
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus();
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在数量
     */
    int checkUsernameExists(String username);
} 