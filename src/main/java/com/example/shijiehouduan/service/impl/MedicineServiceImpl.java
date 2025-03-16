package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.MedicineDao;
import com.example.shijiehouduan.entity.Medicine;
import com.example.shijiehouduan.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 药品服务实现类
 */
@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineDao medicineDao;

    @Override
    public Medicine getMedicineById(Integer medicineId) {
        return medicineDao.findById(medicineId);
    }

    @Override
    public List<Medicine> getMedicinesByName(String name) {
        return medicineDao.findByName(name);
    }

    @Override
    public List<Medicine> getMedicinesByManufacturer(String manufacturer) {
        return medicineDao.findByManufacturer(manufacturer);
    }

    @Override
    public List<Medicine> getAllMedicines() {
        return medicineDao.findAll();
    }

    @Override
    public List<Medicine> getLowStockMedicines(Integer threshold) {
        return medicineDao.findLowStock(threshold);
    }

    @Override
    public List<Medicine> getNearExpiryMedicines() {
        return medicineDao.findNearExpiry();
    }

    @Override
    @Transactional
    public boolean addMedicine(Medicine medicine) {
        // 设置创建时间
        if (medicine.getCreatedAt() == null) {
            medicine.setCreatedAt(new Date());
        }
        
        // 如果库存为空，设置为0
        if (medicine.getStock() == null) {
            medicine.setStock(0);
        }
        
        return medicineDao.insert(medicine) > 0;
    }

    @Override
    @Transactional
    public boolean updateMedicine(Medicine medicine) {
        // 检查药品是否存在
        Medicine existingMedicine = medicineDao.findById(medicine.getMedicineId());
        if (existingMedicine == null) {
            return false;
        }
        
        return medicineDao.update(medicine) > 0;
    }

    @Override
    @Transactional
    public boolean updateMedicineStock(Integer medicineId, Integer stock) {
        // 检查药品是否存在
        Medicine existingMedicine = medicineDao.findById(medicineId);
        if (existingMedicine == null) {
            return false;
        }
        
        // 库存不能为负数
        if (stock < 0) {
            stock = 0;
        }
        
        return medicineDao.updateStock(medicineId, stock) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMedicine(Integer medicineId) {
        // 检查药品是否存在
        Medicine existingMedicine = medicineDao.findById(medicineId);
        if (existingMedicine == null) {
            return false;
        }
        
        return medicineDao.delete(medicineId) > 0;
    }

    @Override
    public long countAllMedicines() {
        return medicineDao.countAll();
    }

    @Override
    public Map<String, Object> countStockValue() {
        return medicineDao.countStockValue();
    }
} 