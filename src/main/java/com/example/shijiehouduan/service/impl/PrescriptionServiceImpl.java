package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.DoctorDao;
import com.example.shijiehouduan.dao.MedicineDao;
import com.example.shijiehouduan.dao.PrescriptionDao;
import com.example.shijiehouduan.dao.PrescriptionMedicineDao;
import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.entity.Medicine;
import com.example.shijiehouduan.entity.Prescription;
import com.example.shijiehouduan.entity.PrescriptionMedicine;
import com.example.shijiehouduan.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处方服务实现类
 */
@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionDao prescriptionDao;

    @Autowired
    private PrescriptionMedicineDao prescriptionMedicineDao;
    
    @Autowired
    private MedicineDao medicineDao;

    @Autowired
    private DoctorDao doctorDao;

    @Override
    public Prescription getPrescriptionById(Integer prescriptionId) {
        return prescriptionDao.findById(prescriptionId);
    }

    @Override
    public List<Prescription> getPrescriptionsByPatientId(Integer patientId) {
        return prescriptionDao.findByPatientId(patientId);
    }

    @Override
    public List<Prescription> getPrescriptionsByDoctorId(Integer doctorId) {
        return prescriptionDao.findByDoctorId(doctorId);
    }

    @Override
    public List<Prescription> getPrescriptionsByRecordId(Integer recordId) {
        return prescriptionDao.findByRecordId(recordId);
    }

    @Override
    public List<Prescription> getPrescriptionsByStatus(String status) {
        return prescriptionDao.findByStatus(status);
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionDao.findAll();
    }

    @Override
    @Transactional
    public Integer addPrescription(Prescription prescription, List<PrescriptionMedicine> prescriptionMedicines) {
        // 设置处方初始状态为待发药
        if (prescription.getStatus() == null) {
            prescription.setStatus("待发药");
        }
        
        // 设置开具日期
        if (prescription.getIssueDate() == null) {
            prescription.setIssueDate(new Date());
        }
        
        // 计算总费用
        BigDecimal totalFee = BigDecimal.ZERO;
        for (PrescriptionMedicine pm : prescriptionMedicines) {
            Medicine medicine = medicineDao.findById(pm.getMedicineId());
            if (medicine != null) {
                BigDecimal price = medicine.getPrice();
                BigDecimal quantity = new BigDecimal(pm.getQuantity());
                BigDecimal fee = price.multiply(quantity);
                
                pm.setFee(fee);
                totalFee = totalFee.add(fee);
            }
        }
        prescription.setTotalFee(totalFee.doubleValue());
        
        // 保存处方信息
        prescriptionDao.insert(prescription);
        Integer prescriptionId = prescription.getPrescriptionId();
        
        // 保存处方药品信息
        for (PrescriptionMedicine pm : prescriptionMedicines) {
            pm.setPrescriptionId(prescriptionId);
            prescriptionMedicineDao.insert(pm);
        }
        
        return prescriptionId;
    }

    @Override
    @Transactional
    public boolean updatePrescription(Prescription prescription) {
        return prescriptionDao.update(prescription) > 0;
    }

    @Override
    @Transactional
    public boolean updatePrescriptionStatus(Integer prescriptionId, String status) {
        return prescriptionDao.updateStatus(prescriptionId, status) > 0;
    }

    @Override
    public Map<String, Object> getPrescriptionDetail(Integer prescriptionId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取处方基本信息
        Prescription prescription = prescriptionDao.findById(prescriptionId);
        if (prescription == null) {
            return null;
        }
        result.put("prescription", prescription);
        
        // 获取医生信息
        Doctor doctor = doctorDao.findById(prescription.getDoctorId());
        result.put("doctor", doctor);
        
        // 获取处方药品信息
        List<Map<String, Object>> medicineDetailList = new ArrayList<>();
        List<PrescriptionMedicine> prescriptionMedicines = prescriptionMedicineDao.findByPrescriptionId(prescriptionId);
        
        for (PrescriptionMedicine pm : prescriptionMedicines) {
            Map<String, Object> medicineDetail = new HashMap<>();
            Medicine medicine = medicineDao.findById(pm.getMedicineId());
            
            medicineDetail.put("prescriptionMedicine", pm);
            medicineDetail.put("medicine", medicine);
            medicineDetailList.add(medicineDetail);
        }
        
        result.put("medicineDetails", medicineDetailList);
        return result;
    }
} 