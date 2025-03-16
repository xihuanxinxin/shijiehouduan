package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.PrescriptionMedicine;
import java.util.List;

/**
 * 处方药品DAO接口
 */
public interface PrescriptionMedicineDao {
    /**
     * 根据ID查询处方药品
     */
    PrescriptionMedicine findById(Integer id);

    /**
     * 根据处方ID查询处方药品列表
     */
    List<PrescriptionMedicine> findByPrescriptionId(Integer prescriptionId);

    /**
     * 根据药品ID查询处方药品列表
     */
    List<PrescriptionMedicine> findByMedicineId(Integer medicineId);

    /**
     * 新增处方药品
     */
    int insert(PrescriptionMedicine prescriptionMedicine);

    /**
     * 批量新增处方药品
     */
    int batchInsert(List<PrescriptionMedicine> prescriptionMedicines);

    /**
     * 更新处方药品
     */
    int update(PrescriptionMedicine prescriptionMedicine);

    /**
     * 删除处方药品
     */
    int delete(Integer id);

    /**
     * 根据处方ID删除处方药品
     */
    int deleteByPrescriptionId(Integer prescriptionId);
} 