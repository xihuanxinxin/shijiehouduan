package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.EyeExaminationDao;
import com.example.shijiehouduan.entity.EyeExamination;
import com.example.shijiehouduan.service.EyeExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 眼科检查记录服务实现类
 */
@Service
public class EyeExaminationServiceImpl implements EyeExaminationService {

    @Autowired
    private EyeExaminationDao eyeExaminationDao;

    @Override
    public EyeExamination getEyeExaminationById(Integer examinationId) {
        return eyeExaminationDao.findById(examinationId);
    }

    @Override
    public List<EyeExamination> getEyeExaminationsByRecordId(Integer recordId) {
        return eyeExaminationDao.findByRecordId(recordId);
    }

    @Override
    public List<EyeExamination> getEyeExaminationsByPatientId(Integer patientId) {
        return eyeExaminationDao.findByPatientId(patientId);
    }

    @Override
    public List<EyeExamination> getEyeExaminationsByDoctorId(Integer doctorId) {
        return eyeExaminationDao.findByDoctorId(doctorId);
    }

    @Override
    public List<EyeExamination> getAllEyeExaminations() {
        return eyeExaminationDao.findAll();
    }
    
    @Override
    @Transactional
    public boolean addEyeExamination(EyeExamination eyeExamination) {
        return eyeExaminationDao.insert(eyeExamination) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateEyeExamination(EyeExamination eyeExamination) {
        return eyeExaminationDao.update(eyeExamination) > 0;
    }

    @Override
    public long countAllEyeExaminations() {
        return eyeExaminationDao.countAll();
    }

    @Override
    public List<Map<String, Object>> countEyeExaminationsByDoctor() {
        return eyeExaminationDao.countByDoctor();
    }
} 