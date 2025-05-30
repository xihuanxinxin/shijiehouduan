package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.UserService;
import com.example.shijiehouduan.utils.DbConnectionTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private UserService userService;

    /**
     * 测试服务是否正常运行
     */
    @GetMapping("/ping")
    public Result ping() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "服务正常运行");
        data.put("timestamp", System.currentTimeMillis());
        return Result.success("服务正常运行", data);
    }
    
    /**
     * 测试数据库连接
     */
    @GetMapping("/db")
    public Result testDb() {
        Map<String, Object> data = new HashMap<>();
        
        // 测试直接JDBC连接
        boolean directConnected = DbConnectionTest.testConnection();
        data.put("directConnection", directConnected ? "成功" : "失败");
        
        // 测试Spring数据源连接
        boolean springConnected = false;
        try {
            Connection conn = dataSource.getConnection();
            springConnected = true;
            conn.close();
        } catch (SQLException e) {
            data.put("springError", e.getMessage());
        }
        data.put("springConnection", springConnected ? "成功" : "失败");
        
        // 测试JdbcTemplate查询
        boolean jdbcTemplateWorks = false;
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
            data.put("userCount", count);
            jdbcTemplateWorks = true;
        } catch (Exception e) {
            data.put("jdbcTemplateError", e.getMessage());
        }
        data.put("jdbcTemplate", jdbcTemplateWorks ? "成功" : "失败");
        
        if (directConnected && springConnected && jdbcTemplateWorks) {
            return Result.success("数据库连接正常", data);
        } else {
            return Result.error(500, "数据库连接异常");
        }
    }
    
    /**
     * 模拟用户登录，在session中设置用户信息
     * @param userId 用户ID
     * @param session HTTP会话
     * @return 结果
     */
    @GetMapping("/mock-login/{userId}")
    public Result mockLogin(@PathVariable("userId") Integer userId, HttpSession session) {
        // 获取完整的用户信息
        User user = userService.getUserById(userId);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 在session中设置完整的用户对象
        session.setAttribute("user", user);
        session.setAttribute("userId", userId);
        session.setAttribute("loginTime", System.currentTimeMillis());
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("username", user.getUsername());
        data.put("roleType", user.getRoleType());
        data.put("sessionId", session.getId());
        
        return Result.success("模拟登录成功，用户信息已存入session", data);
    }
    
    /**
     * 检查会话状态，用于调试
     * @param request HTTP请求
     * @param session HTTP会话
     * @return 会话诊断信息
     */
    @GetMapping("/check-session")
    public Result checkSession(HttpServletRequest request, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        
        // 1. 会话基本信息
        data.put("sessionId", session.getId());
        data.put("sessionCreationTime", new Date(session.getCreationTime()));
        data.put("sessionLastAccessedTime", new Date(session.getLastAccessedTime()));
        data.put("sessionMaxInactiveInterval", session.getMaxInactiveInterval() + "秒");
        data.put("isNewSession", session.isNew());
        
        // 2. 会话中的用户信息
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getUserId());
            userData.put("username", user.getUsername());
            userData.put("roleType", user.getRoleType());
            userData.put("status", user.getStatus());
            data.put("userInfo", userData);
            data.put("isLoggedIn", true);
        } else {
            data.put("isLoggedIn", false);
        }
        
        // 3. 会话中的所有属性
        Map<String, Object> sessionAttributes = new HashMap<>();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            sessionAttributes.put(name, session.getAttribute(name));
        }
        data.put("sessionAttributes", sessionAttributes);
        
        // 4. 请求中的所有Cookie
        Cookie[] cookies = request.getCookies();
        Map<String, String> cookieData = new HashMap<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieData.put(cookie.getName(), cookie.getValue() + 
                              " (Path: " + cookie.getPath() + 
                              ", Domain: " + cookie.getDomain() + 
                              ", MaxAge: " + cookie.getMaxAge() + ")");
            }
        }
        data.put("cookies", cookieData);
        
        // 5. 请求头信息
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        data.put("headers", headers);
        
        return Result.success("会话状态检查", data);
    }
} 