package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 用户登录
     */
    Map<String, Object> login(String username, String password);
    
    /**
     * 根据ID查询用户
     */
    User getUserById(Integer userId);

    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);

    /**
     * 新增用户
     */
    boolean addUser(User user);
    
    /**
     * 查询所有用户
     */
    List<User> getAllUsers();
    
    /**
     * 根据角色类型查询用户
     */
    List<User> getUsersByRoleType(String roleType);
    
    /**
     * 根据角色类型分页查询用户
     */
    List<User> getUsersByRoleTypePage(String roleType, Integer pageNum, Integer pageSize);
    
    /**
     * 根据状态查询用户
     */
    List<User> getUsersByStatus(String status);
    
    /**
     * 更新用户
     */
    boolean updateUser(User user);
    
    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Integer userId, String status);
    
    /**
     * 删除用户
     */
    boolean deleteUser(Integer userId);
    
    /**
     * 根据姓名模糊查询用户
     */
    List<User> getUsersByNameLike(String name);
    
    /**
     * 根据电话号码查询用户
     */
    User getUserByPhone(String phone);
} 