package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.SystemLogDao;
import com.example.shijiehouduan.entity.SystemLog;
import com.example.shijiehouduan.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统日志服务实现类
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogDao systemLogDao;

    @Override
    public SystemLog getLogById(Integer logId) {
        return systemLogDao.findById(logId);
    }

    @Override
    public List<SystemLog> getLogsByUserId(Integer userId) {
        return systemLogDao.findByUserId(userId);
    }

    @Override
    public List<SystemLog> getLogsByAction(String action) {
        return systemLogDao.findByAction(action);
    }

    @Override
    public List<SystemLog> getLogsByIpAddress(String ipAddress) {
        return systemLogDao.findByIpAddress(ipAddress);
    }

    @Override
    public List<SystemLog> getLogsByTimeRange(Date startTime, Date endTime) {
        return systemLogDao.findByTimeRange(startTime, endTime);
    }

    @Override
    public List<SystemLog> getAllLogs() {
        return systemLogDao.findAll();
    }

    @Override
    public List<SystemLog> getLogsByPage(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return systemLogDao.findByPage(offset, pageSize);
    }

    @Override
    @Transactional
    public boolean addLog(Integer userId, String action, String content, String ipAddress) {
        SystemLog systemLog = new SystemLog();
        systemLog.setUserId(userId);
        systemLog.setAction(action);
        systemLog.setContent(content);
        systemLog.setIpAddress(ipAddress);
        systemLog.setCreatedAt(new Date());
        return systemLogDao.insert(systemLog) > 0;
    }

    @Override
    @Transactional
    public boolean addLog(SystemLog systemLog) {
        if (systemLog.getCreatedAt() == null) {
            systemLog.setCreatedAt(new Date());
        }
        return systemLogDao.insert(systemLog) > 0;
    }

    @Override
    @Transactional
    public boolean deleteLog(Integer logId) {
        return systemLogDao.delete(logId) > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteLogs(List<Integer> logIds) {
        return systemLogDao.batchDelete(logIds) > 0;
    }

    @Override
    @Transactional
    public boolean deleteLogsBeforeTime(Date endTime) {
        return systemLogDao.deleteBeforeTime(endTime) > 0;
    }

    @Override
    public long countAllLogs() {
        return systemLogDao.countAll();
    }

    @Override
    public List<Map<String, Object>> countLogsByAction() {
        return systemLogDao.countByAction();
    }

    @Override
    public List<Map<String, Object>> countLogsByUser() {
        return systemLogDao.countByUser();
    }

    @Override
    public long countLogsByTimeRange(Date startTime, Date endTime) {
        return systemLogDao.countByTimeRange(startTime, endTime);
    }
}