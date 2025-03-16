package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.BedDao;
import com.example.shijiehouduan.entity.Bed;
import com.example.shijiehouduan.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 床位服务实现类
 */
@Service
public class BedServiceImpl implements BedService {

    @Autowired
    private BedDao bedDao;

    @Override
    public Bed getBedById(Integer bedId) {
        return bedDao.findById(bedId);
    }

    @Override
    public List<Bed> getAllBeds() {
        return bedDao.findAll();
    }

    @Override
    public List<Bed> getBedsByStatus(String status) {
        return bedDao.findByStatus(status);
    }

    @Override
    public List<Bed> getBedsByRoomNumber(String roomNumber) {
        return bedDao.findByRoomNumber(roomNumber);
    }

    @Override
    public Bed getBedByCurrentPatientId(Integer patientId) {
        return bedDao.findByCurrentPatientId(patientId);
    }

    @Override
    @Transactional
    public boolean addBed(Bed bed) {
        // 设置默认状态为空闲
        if (bed.getStatus() == null) {
            bed.setStatus("空闲");
        }
        
        return bedDao.insert(bed) > 0;
    }

    @Override
    @Transactional
    public boolean updateBed(Bed bed) {
        // 检查床位是否存在
        Bed existingBed = bedDao.findById(bed.getBedId());
        if (existingBed == null) {
            return false;
        }
        
        return bedDao.update(bed) > 0;
    }

    @Override
    @Transactional
    public boolean updateBedStatus(Integer bedId, String status) {
        // 检查床位是否存在
        Bed existingBed = bedDao.findById(bedId);
        if (existingBed == null) {
            return false;
        }
        
        return bedDao.updateStatus(bedId, status) > 0;
    }

    @Override
    @Transactional
    public boolean assignBedToPatient(Integer bedId, Integer patientId) {
        // 检查床位是否存在
        Bed existingBed = bedDao.findById(bedId);
        if (existingBed == null) {
            return false;
        }
        
        // 检查床位是否空闲
        if (!"空闲".equals(existingBed.getStatus())) {
            return false;
        }
        
        // 分配床位给患者并更新状态为占用
        boolean assignResult = bedDao.assignToPatient(bedId, patientId) > 0;
        if (assignResult) {
            return bedDao.updateStatus(bedId, "占用") > 0;
        }
        
        return false;
    }

    @Override
    @Transactional
    public boolean releaseBed(Integer bedId) {
        // 检查床位是否存在
        Bed existingBed = bedDao.findById(bedId);
        if (existingBed == null) {
            return false;
        }
        
        // 检查床位是否被占用
        if (!"占用".equals(existingBed.getStatus())) {
            return false;
        }
        
        // 释放床位并更新状态为空闲
        boolean releaseResult = bedDao.release(bedId) > 0;
        if (releaseResult) {
            return bedDao.updateStatus(bedId, "空闲") > 0;
        }
        
        return false;
    }

    @Override
    @Transactional
    public boolean deleteBed(Integer bedId) {
        // 检查床位是否存在
        Bed existingBed = bedDao.findById(bedId);
        if (existingBed == null) {
            return false;
        }
        
        // 检查床位是否空闲
        if (!"空闲".equals(existingBed.getStatus())) {
            return false;
        }
        
        return bedDao.delete(bedId) > 0;
    }
} 