package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Medicine;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 药品DAO接口
 */
public interface MedicineDao {
    /**
     * 根据ID查询药品
     */
    Medicine findById(Integer medicineId);

    /**
     * 根据名称查询药品
     */
    List<Medicine> findByName(String name);

    /**
     * 根据生产厂家查询药品
     */
    List<Medicine> findByManufacturer(String manufacturer);

    /**
     * 查询所有药品
     */
    List<Medicine> findAll();

    /**
     * 查询库存不足的药品
     */
    List<Medicine> findLowStock(Integer threshold);

    /**
     * 查询即将过期的药品
     */
    List<Medicine> findNearExpiry();

    /**
     * 新增药品
     */
    int insert(Medicine medicine);

    /**
     * 更新药品
     */
    int update(Medicine medicine);

    /**
     * 更新药品库存
     */
    int updateStock(@Param("medicineId") Integer medicineId, @Param("stock") Integer stock);

    /**
     * 删除药品
     */
    int delete(Integer medicineId);

    /**
     * 统计药品总数
     */
    long countAll();

    /**
     * 统计库存总值
     */
    Map<String, Object> countStockValue();
} 