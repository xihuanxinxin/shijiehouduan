package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.SystemLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统日志服务接口
 */
public interface SystemLogService {
    
    /**
     * 根据ID查询日志
     * @param logId 日志ID
     * @return 日志记录
     */
    SystemLog getLogById(Integer logId);
    
    /**
     * 根据用户ID查询日志
     * @param userId 用户ID
     * @return 日志记录列表
     */
    List<SystemLog> getLogsByUserId(Integer userId);
    
    /**
     * 根据操作类型查询日志
     * @param action 操作类型
     * @return 日志记录列表
     */
    List<SystemLog> getLogsByAction(String action);
    
    /**
     * 根据IP地址查询日志
     * @param ipAddress IP地址
     * @return 日志记录列表
     */
    List<SystemLog> getLogsByIpAddress(String ipAddress);
    
    /**
     * 根据时间范围查询日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日志记录列表
     */
    List<SystemLog> getLogsByTimeRange(Date startTime, Date endTime);
    
    /**
     * 查询所有日志
     * @return 日志记录列表
     */
    List<SystemLog> getAllLogs();
    
    /**
     * 分页查询日志
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return 日志记录列表
     */
    List<SystemLog> getLogsByPage(Integer pageNum, Integer pageSize);
    
    /**
     * 添加日志
     * @param userId 用户ID
     * @param action 操作类型
     * @param content 操作内容
     * @param ipAddress IP地址
     * @return 是否成功
     */
    boolean addLog(Integer userId, String action, String content, String ipAddress);
    
    /**
     * 添加日志
     * @param systemLog 日志记录
     * @return 是否成功
     */
    boolean addLog(SystemLog systemLog);
    
    /**
     * 删除日志
     * @param logId 日志ID
     * @return 是否成功
     */
    boolean deleteLog(Integer logId);
    
    /**
     * 批量删除日志
     * @param logIds 日志ID列表
     * @return 是否成功
     */
    boolean batchDeleteLogs(List<Integer> logIds);
    
    /**
     * 清空指定时间之前的日志
     * @param endTime 结束时间
     * @return 是否成功
     */
    boolean deleteLogsBeforeTime(Date endTime);
    
    /**
     * 统计日志总数
     * @return 日志总数
     */
    long countAllLogs();
    
    /**
     * 统计各操作类型的日志数量
     * @return 统计结果
     */
    List<Map<String, Object>> countLogsByAction();
    
    /**
     * 统计各用户的操作日志数量
     * @return 统计结果
     */
    List<Map<String, Object>> countLogsByUser();
    
    /**
     * 统计指定时间范围内的日志数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    long countLogsByTimeRange(Date startTime, Date endTime);
} 