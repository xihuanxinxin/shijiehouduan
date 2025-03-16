package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.SystemLog;
import com.example.shijiehouduan.service.SystemLogService;
import com.example.shijiehouduan.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统日志控制器
 */
@RestController
@RequestMapping("/api/system/logs")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    /**
     * 根据ID查询日志
     * @param logId 日志ID
     * @return 结果
     */
    @GetMapping("/{logId}")
    public Result getLogById(@PathVariable Integer logId) {
        try {
            SystemLog log = systemLogService.getLogById(logId);
            if (log != null) {
                return Result.success(log);
            } else {
                return Result.error("日志不存在");
            }
        } catch (Exception e) {
            return Result.error("查询日志失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询日志
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/user/{userId}")
    public Result getLogsByUserId(@PathVariable Integer userId) {
        try {
            List<SystemLog> logs = systemLogService.getLogsByUserId(userId);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error("查询用户日志失败: " + e.getMessage());
        }
    }

    /**
     * 根据操作类型查询日志
     * @param action 操作类型
     * @return 结果
     */
    @GetMapping("/action/{action}")
    public Result getLogsByAction(@PathVariable String action) {
        try {
            List<SystemLog> logs = systemLogService.getLogsByAction(action);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error("查询操作日志失败: " + e.getMessage());
        }
    }

    /**
     * 根据IP地址查询日志
     * @param ipAddress IP地址
     * @return 结果
     */
    @GetMapping("/ip/{ipAddress}")
    public Result getLogsByIpAddress(@PathVariable String ipAddress) {
        try {
            List<SystemLog> logs = systemLogService.getLogsByIpAddress(ipAddress);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error("查询IP日志失败: " + e.getMessage());
        }
    }

    /**
     * 根据时间范围查询日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 结果
     */
    @GetMapping("/time-range")
    public Result getLogsByTimeRange(
            @RequestParam Date startTime,
            @RequestParam Date endTime) {
        try {
            List<SystemLog> logs = systemLogService.getLogsByTimeRange(startTime, endTime);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error("查询时间范围日志失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有日志
     * @return 结果
     */
    @GetMapping("/all")
    public Result getAllLogs() {
        try {
            List<SystemLog> logs = systemLogService.getAllLogs();
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error("查询所有日志失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询日志
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return 结果
     */
    @GetMapping("/page")
    public Result getLogsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            List<SystemLog> logs = systemLogService.getLogsByPage(pageNum, pageSize);
            long total = systemLogService.countAllLogs();
            Map<String, Object> data = new HashMap<>();
            data.put("list", logs);
            data.put("total", total);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("分页查询日志失败: " + e.getMessage());
        }
    }

    /**
     * 添加日志
     * @param systemLog 日志记录
     * @param request HTTP请求
     * @return 结果
     */
    @PostMapping
    public Result addLog(@RequestBody SystemLog systemLog, HttpServletRequest request) {
        try {
            // 设置IP地址
            if (systemLog.getIpAddress() == null) {
                systemLog.setIpAddress(getIpAddress(request));
            }
            
            boolean success = systemLogService.addLog(systemLog);
            if (success) {
                return Result.success("添加日志成功");
            } else {
                return Result.error("添加日志失败");
            }
        } catch (Exception e) {
            return Result.error("添加日志失败: " + e.getMessage());
        }
    }

    /**
     * 删除日志
     * @param logId 日志ID
     * @return 结果
     */
    @DeleteMapping("/{logId}")
    public Result deleteLog(@PathVariable Integer logId) {
        try {
            boolean success = systemLogService.deleteLog(logId);
            if (success) {
                return Result.success("删除日志成功");
            } else {
                return Result.error("删除日志失败");
            }
        } catch (Exception e) {
            return Result.error("删除日志失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除日志
     * @param logIds 日志ID列表
     * @return 结果
     */
    @DeleteMapping("/batch")
    public Result batchDeleteLogs(@RequestBody List<Integer> logIds) {
        try {
            boolean success = systemLogService.batchDeleteLogs(logIds);
            if (success) {
                return Result.success("批量删除日志成功");
            } else {
                return Result.error("批量删除日志失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除日志失败: " + e.getMessage());
        }
    }

    /**
     * 清空指定时间之前的日志
     * @param endTime 结束时间
     * @return 结果
     */
    @DeleteMapping("/before-time")
    public Result deleteLogsBeforeTime(@RequestParam Date endTime) {
        try {
            boolean success = systemLogService.deleteLogsBeforeTime(endTime);
            if (success) {
                return Result.success("清空日志成功");
            } else {
                return Result.error("清空日志失败");
            }
        } catch (Exception e) {
            return Result.error("清空日志失败: " + e.getMessage());
        }
    }

    /**
     * 统计日志总数
     * @return 结果
     */
    @GetMapping("/count")
    public Result countAllLogs() {
        try {
            long count = systemLogService.countAllLogs();
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计日志失败: " + e.getMessage());
        }
    }

    /**
     * 统计各操作类型的日志数量
     * @return 结果
     */
    @GetMapping("/count/action")
    public Result countLogsByAction() {
        try {
            List<Map<String, Object>> data = systemLogService.countLogsByAction();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计操作类型日志失败: " + e.getMessage());
        }
    }

    /**
     * 统计各用户的操作日志数量
     * @return 结果
     */
    @GetMapping("/count/user")
    public Result countLogsByUser() {
        try {
            List<Map<String, Object>> data = systemLogService.countLogsByUser();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计用户日志失败: " + e.getMessage());
        }
    }

    /**
     * 统计指定时间范围内的日志数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 结果
     */
    @GetMapping("/count/time-range")
    public Result countLogsByTimeRange(
            @RequestParam Date startTime,
            @RequestParam Date endTime) {
        try {
            long count = systemLogService.countLogsByTimeRange(startTime, endTime);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计时间范围日志失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取客户端IP地址
     * @param request HTTP请求
     * @return IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 