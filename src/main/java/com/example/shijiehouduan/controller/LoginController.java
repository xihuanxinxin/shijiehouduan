package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.dto.LoginRequest;
import com.example.shijiehouduan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录 - 表单提交方式
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
    public Result loginForm(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> loginResult = userService.login(username, password);
        
        if ((Boolean) loginResult.get("success")) {
            return Result.success(loginResult);
        } else {
            return Result.error(loginResult.get("message").toString());
        }
    }
    
    /**
     * 用户登录 - JSON提交方式
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public Result loginJson(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> loginResult = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        
        if ((Boolean) loginResult.get("success")) {
            return Result.success(loginResult);
        } else {
            return Result.error(loginResult.get("message").toString());
        }
    }
} 