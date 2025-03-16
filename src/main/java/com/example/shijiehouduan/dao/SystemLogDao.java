package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统日志数据访问接口
 */
@Mapper
public interface SystemLogDao {
    
    /**
     * 根据ID查询日志
     * @param logId 日志ID
     * @return 日志记录
     */
    SystemLog findById(Integer logId);
    
    /**
     * 根据用户ID查询日志
     * @param userId 用户ID
     * @return 日志记录列表
     */
    List<SystemLog> findByUserId(Integer userId);
    
    /**
     * 根据操作类型查询日志
     * @param action 操作类型
     * @return 日志记录列表
     */
    List<SystemLog> findByAction(String action);
    
    /**
     * 根据IP地址查询日志
     * @param ipAddress IP地址
     * @return 日志记录列表
     */
    List<SystemLog> findByIpAddress(String ipAddress);
    
    /**
     * 根据时间范围查询日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日志记录列表
     */
    List<SystemLog> findByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    
    /**
     * 查询所有日志
     * @return 日志记录列表
     */
    List<SystemLog> findAll();
    
    /**
     * 分页查询日志
     * @param offset 偏移量
     * @param limit 每页记录数
     * @return 日志记录列表
     */
    List<SystemLog> findByPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 新增日志
     * @param systemLog 日志记录
     * @return 影响行数
     */
    int insert(SystemLog systemLog);
    
    /**
     * 删除日志
     * @param logId 日志ID
     * @return 影响行数
     */
    int delete(Integer logId);
    
    /**
     * 批量删除日志
     * @param logIds 日志ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("logIds") List<Integer> logIds);
    
    /**
     * 清空指定时间之前的日志
     * @param endTime 结束时间
     * @return 影响行数
     */
    int deleteBeforeTime(@Param("endTime") Date endTime);
    
    /**
     * 统计日志总数
     * @return 日志总数
     */
    long countAll();
    
    /**
     * 统计各操作类型的日志数量
     * @return 统计结果
     */
    List<Map<String, Object>> countByAction();
    
    /**
     * 统计各用户的操作日志数量
     * @return 统计结果
     */
    List<Map<String, Object>> countByUser();
    
    /**
     * 统计指定时间范围内的日志数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    long countByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
} 