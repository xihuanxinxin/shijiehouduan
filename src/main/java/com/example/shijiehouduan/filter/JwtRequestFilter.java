package com.example.shijiehouduan.filter;

import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.UserService;
import com.example.shijiehouduan.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT请求过滤器，用于验证请求中的token
 */
public class JwtRequestFilter implements Filter {

    private UserService userService;
    private JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 从ServletContext获取Spring容器，并从中获取Bean
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        
        // 从Spring容器中获取所需的Bean
        if (webApplicationContext != null) {
            this.userService = webApplicationContext.getBean(UserService.class);
            this.jwtUtil = webApplicationContext.getBean(JwtUtil.class);
        }
        
        // 检查Bean是否获取成功
        if (this.userService == null || this.jwtUtil == null) {
            throw new ServletException("无法获取Spring Bean: UserService或JwtUtil");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 获取Authorization头
        final String authorizationHeader = httpRequest.getHeader("Authorization");

        // 对于OPTIONS请求（预检请求）直接放行
        if (httpRequest.getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        // 不需要token的路径直接放行
        String path = httpRequest.getRequestURI();
        if (path.contains("/auth/login") || path.contains("/test/") || path.contains(".html") || 
            path.contains(".js") || path.contains(".css") || path.contains(".ico")) {
            chain.doFilter(request, response);
            return;
        }
        
        Integer userId = null;
        String jwt = null;

        // 从Authorization头中提取token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                userId = jwtUtil.getUserIdFromToken(jwt);
            } catch (Exception e) {
                System.out.println("JWT Token解析失败: " + e.getMessage());
            }
        }

        // 验证token并设置用户信息到请求中
        if (userId != null && jwtUtil.validateToken(jwt)) {
            User user = userService.getUserById(userId);
            
            if (user != null) {
                // 将用户信息存入session，以兼容现有代码
                httpRequest.getSession().setAttribute("user", user);
                httpRequest.getSession().setAttribute("userId", userId);
                httpRequest.getSession().setAttribute("loginTime", System.currentTimeMillis());
                
                // 继续过滤器链
                chain.doFilter(request, response);
                return;
            }
        }
        
        // 检查session中是否已有用户信息（兼容旧代码）
        if (httpRequest.getSession().getAttribute("user") != null) {
            chain.doFilter(request, response);
            return;
        }
        
        // 未认证的请求返回401状态码
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未授权的访问");
    }

    @Override
    public void destroy() {
        // 无需特殊处理
    }
} 