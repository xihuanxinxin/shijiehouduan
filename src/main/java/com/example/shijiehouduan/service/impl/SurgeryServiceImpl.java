package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.SurgeryDao;
import com.example.shijiehouduan.entity.Surgery;
import com.example.shijiehouduan.service.SurgeryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 手术服务实现类
 */
@Service
public class SurgeryServiceImpl implements SurgeryService {

    @Autowired
    private SurgeryDao surgeryDao;

    @Override
    public Surgery getSurgeryById(Integer surgeryId) {
        return surgeryDao.findById(surgeryId);
    }

    @Override
    public List<Surgery> getSurgeriesByRecordId(Integer recordId) {
        return surgeryDao.findByRecordId(recordId);
    }

    @Override
    public List<Surgery> getSurgeriesByPatientId(Integer patientId) {
        return surgeryDao.findByPatientId(patientId);
    }

    @Override
    public List<Surgery> getSurgeriesByDoctorId(Integer doctorId) {
        return surgeryDao.findByDoctorId(doctorId);
    }

    @Override
    public List<Surgery> getSurgeriesByStatus(String status) {
        return surgeryDao.findByStatus(status);
    }

    @Override
    public List<Surgery> getSurgeriesByType(String surgeryType) {
        return surgeryDao.findByType(surgeryType);
    }

    @Override
    public List<Surgery> getAllSurgeries() {
        return surgeryDao.findAll();
    }

    @Override
    @Transactional
    public boolean addSurgery(Surgery surgery) {
        // 设置默认状态为待手术
        if (surgery.getStatus() == null) {
            surgery.setStatus("待手术");
        }
        
        // 设置创建时间
        if (surgery.getCreatedAt() == null) {
            surgery.setCreatedAt(new Date());
        }
        
        return surgeryDao.insert(surgery) > 0;
    }

    @Override
    @Transactional
    public boolean updateSurgery(Surgery surgery) {
        // 检查手术记录是否存在
        Surgery existingSurgery = surgeryDao.findById(surgery.getSurgeryId());
        if (existingSurgery == null) {
            return false;
        }
        
        return surgeryDao.update(surgery) > 0;
    }

    @Override
    @Transactional
    public boolean updateSurgeryStatus(Integer surgeryId, String status) {
        // 检查手术记录是否存在
        Surgery existingSurgery = surgeryDao.findById(surgeryId);
        if (existingSurgery == null) {
            return false;
        }
        
        // 检查状态是否有效
        if (!isValidStatus(status)) {
            return false;
        }
        
        return surgeryDao.updateStatus(surgeryId, status) > 0;
    }

    @Override
    public long countSurgeriesByStatus(String status) {
        return surgeryDao.countByStatus(status);
    }

    @Override
    public List<Map<String, Object>> countSurgeriesByType() {
        return surgeryDao.countByType();
    }
    
    /**
     * 检查状态是否有效
     */
    private boolean isValidStatus(String status) {
        return "待手术".equals(status) || "手术中".equals(status) || "已完成".equals(status) || "已取消".equals(status);
    }
} 