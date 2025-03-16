package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.PrescriptionMedicine;
import java.util.List;

/**
 * 处方药品服务接口
 */
public interface PrescriptionMedicineService {
    /**
     * 根据ID查询处方药品
     */
    PrescriptionMedicine getPrescriptionMedicineById(Integer id);

    /**
     * 根据处方ID查询处方药品列表
     */
    List<PrescriptionMedicine> getPrescriptionMedicinesByPrescriptionId(Integer prescriptionId);

    /**
     * 根据药品ID查询处方药品列表
     */
    List<PrescriptionMedicine> getPrescriptionMedicinesByMedicineId(Integer medicineId);

    /**
     * 添加处方药品
     */
    boolean addPrescriptionMedicine(PrescriptionMedicine prescriptionMedicine);

    /**
     * 批量添加处方药品
     */
    boolean batchAddPrescriptionMedicines(List<PrescriptionMedicine> prescriptionMedicines);

    /**
     * 更新处方药品
     */
    boolean updatePrescriptionMedicine(PrescriptionMedicine prescriptionMedicine);

    /**
     * 删除处方药品
     */
    boolean deletePrescriptionMedicine(Integer id);

    /**
     * 根据处方ID删除处方药品
     */
    boolean deletePrescriptionMedicinesByPrescriptionId(Integer prescriptionId);
} 