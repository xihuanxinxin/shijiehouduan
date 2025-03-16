package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.MedicalRecordDao;
import com.example.shijiehouduan.entity.MedicalRecord;
import com.example.shijiehouduan.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 病历服务实现类
 */
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordDao medicalRecordDao;

    @Override
    public MedicalRecord getMedicalRecordById(Integer recordId) {
        return medicalRecordDao.findById(recordId);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Integer patientId) {
        return medicalRecordDao.findByPatientId(patientId);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByDoctorId(Integer doctorId) {
        return medicalRecordDao.findByDoctorId(doctorId);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByStatus(String status) {
        return medicalRecordDao.findByStatus(status);
    }

    @Override
    @Transactional
    public boolean addMedicalRecord(MedicalRecord medicalRecord) {
        // 设置默认状态为待诊
        if (medicalRecord.getStatus() == null) {
            medicalRecord.setStatus("待诊");
        }
        
        // 设置就诊日期
        if (medicalRecord.getVisitDate() == null) {
            medicalRecord.setVisitDate(new Date());
        }
        
        // 设置创建时间
        if (medicalRecord.getCreatedAt() == null) {
            medicalRecord.setCreatedAt(new Date());
        }
        
        return medicalRecordDao.insert(medicalRecord) > 0;
    }

    @Override
    @Transactional
    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordDao.update(medicalRecord) > 0;
    }

    @Override
    @Transactional
    public boolean setMedicalRecordPending(Integer recordId) {
        return medicalRecordDao.updateStatus(recordId, "待诊") > 0;
    }

    @Override
    @Transactional
    public boolean setMedicalRecordInProgress(Integer recordId) {
        return medicalRecordDao.updateStatus(recordId, "诊疗中") > 0;
    }

    @Override
    @Transactional
    public boolean setMedicalRecordCompleted(Integer recordId) {
        return medicalRecordDao.updateStatus(recordId, "已完成") > 0;
    }

    @Override
    @Transactional
    public boolean setMedicalRecordCancelled(Integer recordId) {
        return medicalRecordDao.updateStatus(recordId, "已取消") > 0;
    }
} 