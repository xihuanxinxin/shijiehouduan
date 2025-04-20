package com.example.shijiehouduan.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理CORS（跨域资源共享）的过滤器
 */
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化时不需要特殊处理
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        // 允许的源(Origin)列表，包括前端开发服务器
        String allowedOrigins = "http://localhost:5500,http://127.0.0.1:5500,http://localhost:8080,http://127.0.0.1:8080";
        
        // 获取请求的Origin
        String origin = request.getHeader("Origin");
        
        // 如果Origin在允许列表中，设置CORS响应头
        if (origin != null && allowedOrigins.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", 
                "Content-Type, Authorization, X-Requested-With, Accept, Origin, Access-Control-Request-Method, Access-Control-Request-Headers, Cookie, Set-Cookie");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
            response.setHeader("Access-Control-Max-Age", "3600");
        }
        
        // 对于OPTIONS请求（预检请求），直接返回200状态码
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // 继续过滤器链
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // 销毁时不需要特殊处理
    }
} 