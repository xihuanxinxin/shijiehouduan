package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.UserDao;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        
        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名不能为空");
            return result;
        }
        
        if (password == null || password.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }
        
        // 查询用户
        User user = userDao.login(username, password);
        
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
            return result;
        }
        
        // 检查用户状态
        if ("禁用".equals(user.getStatus())) {
            result.put("success", false);
            result.put("message", "账号已被禁用，请联系管理员");
            return result;
        }
        
        // 登录成功
        result.put("success", true);
        result.put("message", "登录成功");
        result.put("user", user);
        
        return result;
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.findById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userDao.findByUsername(username) != null;
    }

    @Override
    @Transactional
    public boolean addUser(User user) {
        // 检查用户名是否已存在
        User existingUser = userDao.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false;
        }
        
        // 设置默认状态为启用
        if (user.getStatus() == null) {
            user.setStatus("启用");
        }
        
        // 设置创建时间
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Date());
        }
        
        return userDao.insert(user) > 0;
    }
    
    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
    
    @Override
    public List<User> getUsersByRoleType(String roleType) {
        return userDao.findByRoleType(roleType);
    }
    
    @Override
    public List<User> getUsersByRoleTypePage(String roleType, Integer pageNum, Integer pageSize) {
        // 计算偏移量
        Integer offset = (pageNum - 1) * pageSize;
        // 调用DAO层的分页方法
        return userDao.findByRoleTypePage(roleType, offset, pageSize);
    }
    
    @Override
    public List<User> getUsersByStatus(String status) {
        return userDao.findByStatus(status);
    }
    
    @Override
    @Transactional
    public boolean updateUser(User user) {
        // 检查用户是否存在
        User existingUser = userDao.findById(user.getUserId());
        if (existingUser == null) {
            return false;
        }
        
        // 如果修改了用户名，检查新用户名是否已存在
        if (!existingUser.getUsername().equals(user.getUsername())) {
            User userWithSameUsername = userDao.findByUsername(user.getUsername());
            if (userWithSameUsername != null) {
                return false;
            }
        }
        
        // 设置更新时间
        user.setUpdatedAt(new Date());
        
        return userDao.update(user) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateUserStatus(Integer userId, String status) {
        // 检查用户是否存在
        User existingUser = userDao.findById(userId);
        if (existingUser == null) {
            return false;
        }
        
        return userDao.updateStatus(userId, status) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteUser(Integer userId) {
        // 检查用户是否存在
        User existingUser = userDao.findById(userId);
        if (existingUser == null) {
            return false;
        }
        
        return userDao.delete(userId) > 0;
    }
    
    @Override
    public List<User> getUsersByNameLike(String name) {
        return userDao.findByNameLike(name);
    }
    
    @Override
    public User getUserByPhone(String phone) {
        return userDao.findByPhone(phone);
    }
} 