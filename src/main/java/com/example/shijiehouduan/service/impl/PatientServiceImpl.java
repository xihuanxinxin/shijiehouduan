package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.PatientDao;
import com.example.shijiehouduan.entity.Patient;
import com.example.shijiehouduan.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 患者服务实现类
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDao patientDao;

    @Override
    public Patient getPatientById(Integer patientId) {
        return patientDao.findById(patientId);
    }

    @Override
    public Patient getPatientByUserId(Integer userId) {
        return patientDao.findByUserId(userId);
    }

    @Override
    public Patient getPatientByIdCard(String idCard) {
        return patientDao.findByIdCard(idCard);
    }

    @Override
    @Transactional
    public boolean addPatient(Patient patient) {
        return patientDao.insert(patient) > 0;
    }

    @Override
    @Transactional
    public boolean updatePatient(Patient patient) {
        return patientDao.update(patient) > 0;
    }
} 