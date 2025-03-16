package com.example.shijiehouduan.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接测试类
 */
public class DbConnectionTest {
    
    // 数据库连接信息
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/shijieshujuku?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    
    /**
     * 获取数据库URL
     */
    public static String getURL() {
        return URL;
    }
    
    /**
     * 测试数据库连接
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            // 加载驱动
            Class.forName(DRIVER);
            // 获取连接
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return conn != null;
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动加载失败: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("数据库连接失败: " + e.getMessage());
            return false;
        } finally {
            // 关闭连接
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        boolean connected = testConnection();
        if (connected) {
            System.out.println("数据库连接成功！");
        } else {
            System.out.println("数据库连接失败！");
        }
    }
} 