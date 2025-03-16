package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 */
@RestController
public class HomeController {

    /**
     * 首页
     */
    @GetMapping("/")
    public Result<Map<String, Object>> home() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "欢迎访问世界后端API");
        data.put("timestamp", System.currentTimeMillis());
        data.put("status", "running");
        
        return Result.success(data, "API服务正常运行");
    }
    
    /**
     * 测试根路径
     */
    @GetMapping("/shijiehouduan_war_exploded")
    public Result<Map<String, Object>> root() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "欢迎访问世界后端API");
        data.put("timestamp", System.currentTimeMillis());
        data.put("status", "running");
        data.put("availableApis", new String[]{
            "/shijiehouduan_war_exploded/test/ping",
            "/shijiehouduan_war_exploded/test/db"
        });
        
        return Result.success(data, "API服务正常运行");
    }
} 