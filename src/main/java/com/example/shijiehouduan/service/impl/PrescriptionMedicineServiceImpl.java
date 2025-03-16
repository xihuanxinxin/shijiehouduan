package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.PrescriptionMedicineDao;
import com.example.shijiehouduan.entity.PrescriptionMedicine;
import com.example.shijiehouduan.service.PrescriptionMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 处方药品服务实现类
 */
@Service
public class PrescriptionMedicineServiceImpl implements PrescriptionMedicineService {

    @Autowired
    private PrescriptionMedicineDao prescriptionMedicineDao;

    @Override
    public PrescriptionMedicine getPrescriptionMedicineById(Integer id) {
        return prescriptionMedicineDao.findById(id);
    }

    @Override
    public List<PrescriptionMedicine> getPrescriptionMedicinesByPrescriptionId(Integer prescriptionId) {
        return prescriptionMedicineDao.findByPrescriptionId(prescriptionId);
    }

    @Override
    public List<PrescriptionMedicine> getPrescriptionMedicinesByMedicineId(Integer medicineId) {
        return prescriptionMedicineDao.findByMedicineId(medicineId);
    }

    @Override
    @Transactional
    public boolean addPrescriptionMedicine(PrescriptionMedicine prescriptionMedicine) {
        return prescriptionMedicineDao.insert(prescriptionMedicine) > 0;
    }

    @Override
    @Transactional
    public boolean batchAddPrescriptionMedicines(List<PrescriptionMedicine> prescriptionMedicines) {
        return prescriptionMedicineDao.batchInsert(prescriptionMedicines) > 0;
    }

    @Override
    @Transactional
    public boolean updatePrescriptionMedicine(PrescriptionMedicine prescriptionMedicine) {
        // 检查处方药品是否存在
        PrescriptionMedicine existingPrescriptionMedicine = prescriptionMedicineDao.findById(prescriptionMedicine.getId());
        if (existingPrescriptionMedicine == null) {
            return false;
        }
        
        return prescriptionMedicineDao.update(prescriptionMedicine) > 0;
    }

    @Override
    @Transactional
    public boolean deletePrescriptionMedicine(Integer id) {
        // 检查处方药品是否存在
        PrescriptionMedicine existingPrescriptionMedicine = prescriptionMedicineDao.findById(id);
        if (existingPrescriptionMedicine == null) {
            return false;
        }
        
        return prescriptionMedicineDao.delete(id) > 0;
    }

    @Override
    @Transactional
    public boolean deletePrescriptionMedicinesByPrescriptionId(Integer prescriptionId) {
        return prescriptionMedicineDao.deleteByPrescriptionId(prescriptionId) > 0;
    }
} 