package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.dto.RegisterRequest;
import com.example.shijiehouduan.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注册控制器
 */
@RestController
@RequestMapping("/auth")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 患者注册 - JSON提交方式
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping(value = "/register", consumes = "application/json")
    public Result register(@RequestBody RegisterRequest registerRequest) {
        // 参数校验
        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
            return Result.validateFailed("用户名不能为空");
        }
        
        if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            return Result.validateFailed("密码不能为空");
        }
        
        if (registerRequest.getConfirmPassword() == null || !registerRequest.getConfirmPassword().equals(registerRequest.getPassword())) {
            return Result.validateFailed("两次输入的密码不一致");
        }
        
        if (registerRequest.getName() == null || registerRequest.getName().trim().isEmpty()) {
            return Result.validateFailed("姓名不能为空");
        }
        
        // 添加身份证号必填验证
        if (registerRequest.getIdCard() == null || registerRequest.getIdCard().trim().isEmpty()) {
            return Result.validateFailed("身份证号不能为空");
        }
        
        // 检查身份证号格式
        if (!isValidIdCard(registerRequest.getIdCard())) {
            return Result.validateFailed("身份证号格式不正确");
        }
        
        // 调用注册服务
        return registerService.registerPatient(registerRequest);
    }
    
    /**
     * 患者注册 - 表单提交方式
     * @return 注册结果
     */
    @PostMapping(value = "/register", consumes = "application/x-www-form-urlencoded")
    public Result registerForm(RegisterRequest registerRequest) {
        // 参数校验
        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
            return Result.validateFailed("用户名不能为空");
        }
        
        if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            return Result.validateFailed("密码不能为空");
        }
        
        if (registerRequest.getConfirmPassword() == null || !registerRequest.getConfirmPassword().equals(registerRequest.getPassword())) {
            return Result.validateFailed("两次输入的密码不一致");
        }
        
        if (registerRequest.getName() == null || registerRequest.getName().trim().isEmpty()) {
            return Result.validateFailed("姓名不能为空");
        }
        
        // 添加身份证号必填验证
        if (registerRequest.getIdCard() == null || registerRequest.getIdCard().trim().isEmpty()) {
            return Result.validateFailed("身份证号不能为空");
        }
        
        // 检查身份证号格式
        if (!isValidIdCard(registerRequest.getIdCard())) {
            return Result.validateFailed("身份证号格式不正确");
        }
        
        // 调用注册服务
        return registerService.registerPatient(registerRequest);
    }
    
    /**
     * 验证身份证号格式
     * @param idCard 身份证号
     * @return 是否有效
     */
    private boolean isValidIdCard(String idCard) {
        // 简单验证：18位或15位，最后一位可能是X
        return idCard != null && (idCard.length() == 18 || idCard.length() == 15) 
               && (idCard.length() != 18 || Character.isDigit(idCard.charAt(17)) || idCard.charAt(17) == 'X' || idCard.charAt(17) == 'x');
    }
} 