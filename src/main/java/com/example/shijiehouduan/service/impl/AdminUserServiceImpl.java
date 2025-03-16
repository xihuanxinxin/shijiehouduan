package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.AdminUserDao;
import com.example.shijiehouduan.entity.AdminUser;
import com.example.shijiehouduan.service.AdminUserService;
import com.example.shijiehouduan.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 系统管理员用户服务实现类
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;
    
    @Autowired
    private SystemLogService systemLogService;

    @Override
    public AdminUser login(String username, String password) {
        AdminUser adminUser = adminUserDao.findByUsername(username);
        if (adminUser != null) {
            // 密码加密比对
            String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
            if (encryptedPassword.equals(adminUser.getPassword())) {
                // 更新最后登录时间
                adminUserDao.updateLastLogin(adminUser.getAdminId(), new Date());
                return adminUser;
            }
        }
        return null;
    }

    @Override
    public AdminUser getAdminById(Integer adminId) {
        return adminUserDao.findById(adminId);
    }

    @Override
    public AdminUser getAdminByUsername(String username) {
        return adminUserDao.findByUsername(username);
    }

    @Override
    public List<AdminUser> getAdminsByRole(String role) {
        return adminUserDao.findByRole(role);
    }

    @Override
    public List<AdminUser> getAdminsByStatus(String status) {
        return adminUserDao.findByStatus(status);
    }

    @Override
    public List<AdminUser> getAllAdmins() {
        return adminUserDao.findAll();
    }

    @Override
    public List<AdminUser> getAdminsByPage(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return adminUserDao.findByPage(offset, pageSize);
    }

    @Override
    @Transactional
    public boolean addAdmin(AdminUser adminUser) {
        // 检查用户名是否存在
        if (checkUsernameExists(adminUser.getUsername())) {
            return false;
        }
        
        // 密码加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(adminUser.getPassword().getBytes());
        adminUser.setPassword(encryptedPassword);
        
        // 设置默认状态
        if (adminUser.getStatus() == null) {
            adminUser.setStatus("启用");
        }
        
        // 设置创建时间
        adminUser.setCreatedAt(new Date());
        
        return adminUserDao.insert(adminUser) > 0;
    }

    @Override
    @Transactional
    public boolean updateAdmin(AdminUser adminUser) {
        AdminUser existingAdmin = adminUserDao.findById(adminUser.getAdminId());
        if (existingAdmin == null) {
            return false;
        }
        
        // 如果修改了用户名，检查新用户名是否存在
        if (!existingAdmin.getUsername().equals(adminUser.getUsername()) && checkUsernameExists(adminUser.getUsername())) {
            return false;
        }
        
        // 不更新密码，密码通过专门的方法更新
        adminUser.setPassword(null);
        
        return adminUserDao.update(adminUser) > 0;
    }

    @Override
    @Transactional
    public boolean updateAdminStatus(Integer adminId, String status) {
        AdminUser existingAdmin = adminUserDao.findById(adminId);
        if (existingAdmin == null) {
            return false;
        }
        
        // 检查状态是否有效
        if (!isValidStatus(status)) {
            return false;
        }
        
        return adminUserDao.updateStatus(adminId, status) > 0;
    }

    @Override
    @Transactional
    public boolean updateAdminPassword(Integer adminId, String oldPassword, String newPassword) {
        AdminUser existingAdmin = adminUserDao.findById(adminId);
        if (existingAdmin == null) {
            return false;
        }
        
        // 验证旧密码
        String encryptedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!encryptedOldPassword.equals(existingAdmin.getPassword())) {
            return false;
        }
        
        // 加密新密码
        String encryptedNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        
        return adminUserDao.updatePassword(adminId, encryptedNewPassword) > 0;
    }

    @Override
    @Transactional
    public String resetAdminPassword(Integer adminId) {
        AdminUser existingAdmin = adminUserDao.findById(adminId);
        if (existingAdmin == null) {
            return null;
        }
        
        // 生成随机密码
        String newPassword = generateRandomPassword(8);
        
        // 加密新密码
        String encryptedNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        
        // 更新密码
        if (adminUserDao.updatePassword(adminId, encryptedNewPassword) > 0) {
            return newPassword;
        }
        
        return null;
    }

    @Override
    @Transactional
    public boolean updateAdminLastLogin(Integer adminId, Date lastLogin) {
        AdminUser existingAdmin = adminUserDao.findById(adminId);
        if (existingAdmin == null) {
            return false;
        }
        
        return adminUserDao.updateLastLogin(adminId, lastLogin) > 0;
    }

    @Override
    @Transactional
    public boolean deleteAdmin(Integer adminId) {
        AdminUser existingAdmin = adminUserDao.findById(adminId);
        if (existingAdmin == null) {
            return false;
        }
        
        // 超级管理员不能删除
        if ("超级管理员".equals(existingAdmin.getRole())) {
            return false;
        }
        
        return adminUserDao.delete(adminId) > 0;
    }

    @Override
    public long countAllAdmins() {
        return adminUserDao.countAll();
    }

    @Override
    public List<Map<String, Object>> countAdminsByRole() {
        return adminUserDao.countByRole();
    }

    @Override
    public List<Map<String, Object>> countAdminsByStatus() {
        return adminUserDao.countByStatus();
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return adminUserDao.checkUsernameExists(username) > 0;
    }
    
    /**
     * 检查状态是否有效
     * @param status 状态
     * @return 是否有效
     */
    private boolean isValidStatus(String status) {
        return "启用".equals(status) || "禁用".equals(status);
    }
    
    /**
     * 生成随机密码
     * @param length 密码长度
     * @return 随机密码
     */
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
} 