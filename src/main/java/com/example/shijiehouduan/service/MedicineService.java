package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Medicine;
import java.util.List;
import java.util.Map;

/**
 * 药品服务接口
 */
public interface MedicineService {
    /**
     * 根据ID查询药品
     */
    Medicine getMedicineById(Integer medicineId);

    /**
     * 根据名称查询药品
     */
    List<Medicine> getMedicinesByName(String name);

    /**
     * 根据生产厂家查询药品
     */
    List<Medicine> getMedicinesByManufacturer(String manufacturer);

    /**
     * 查询所有药品
     */
    List<Medicine> getAllMedicines();

    /**
     * 查询库存不足的药品
     */
    List<Medicine> getLowStockMedicines(Integer threshold);

    /**
     * 查询即将过期的药品
     */
    List<Medicine> getNearExpiryMedicines();

    /**
     * 添加药品
     */
    boolean addMedicine(Medicine medicine);

    /**
     * 更新药品
     */
    boolean updateMedicine(Medicine medicine);

    /**
     * 更新药品库存
     */
    boolean updateMedicineStock(Integer medicineId, Integer stock);

    /**
     * 删除药品
     */
    boolean deleteMedicine(Integer medicineId);

    /**
     * 统计药品总数
     */
    long countAllMedicines();

    /**
     * 统计库存总值
     */
    Map<String, Object> countStockValue();
} 