package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户DAO接口
 */
public interface UserDao {
    /**
     * 根据ID查询用户
     */
    User findById(Integer userId);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 用户登录
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 新增用户
     */
    int insert(User user);
    
    /**
     * 查询所有用户
     */
    List<User> findAll();
    
    /**
     * 根据角色类型查询用户
     */
    List<User> findByRoleType(String roleType);
    
    /**
     * 根据角色类型查询用户（分页）
     */
    List<User> findByRoleTypePage(@Param("roleType") String roleType, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 根据状态查询用户
     */
    List<User> findByStatus(String status);
    
    /**
     * 更新用户
     */
    int update(User user);
    
    /**
     * 更新用户状态
     */
    int updateStatus(@Param("userId") Integer userId, @Param("status") String status);
    
    /**
     * 删除用户
     */
    int delete(Integer userId);
    
    /**
     * 根据姓名模糊查询用户
     */
    List<User> findByNameLike(String name);
    
    /**
     * 根据电话号码查询用户
     */
    User findByPhone(String phone);
} 