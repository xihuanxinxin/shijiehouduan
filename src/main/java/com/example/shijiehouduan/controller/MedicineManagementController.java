package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.Medicine;
import com.example.shijiehouduan.entity.PrescriptionMedicine;
import com.example.shijiehouduan.service.MedicineService;
import com.example.shijiehouduan.service.PrescriptionMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药品耗材管理控制器
 */
@RestController
@RequestMapping("/api/admin/medicines")
public class MedicineManagementController {

    @Autowired
    private MedicineService medicineService;
    
    @Autowired
    private PrescriptionMedicineService prescriptionMedicineService;

    /**
     * 获取所有药品
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllMedicines() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Medicine> medicines = medicineService.getAllMedicines();
            response.put("success", true);
            response.put("message", "获取药品列表成功");
            response.put("data", medicines);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取药品列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据ID获取药品
     */
    @GetMapping("/{medicineId}")
    public ResponseEntity<Map<String, Object>> getMedicineById(@PathVariable Integer medicineId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Medicine medicine = medicineService.getMedicineById(medicineId);
            if (medicine != null) {
                response.put("success", true);
                response.put("message", "获取药品成功");
                response.put("data", medicine);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "药品不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取药品失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据名称搜索药品
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchMedicinesByName(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Medicine> medicines = medicineService.getMedicinesByName(name);
            response.put("success", true);
            response.put("message", "搜索药品成功");
            response.put("data", medicines);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "搜索药品失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据生产厂家搜索药品
     */
    @GetMapping("/manufacturer")
    public ResponseEntity<Map<String, Object>> searchMedicinesByManufacturer(@RequestParam String manufacturer) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Medicine> medicines = medicineService.getMedicinesByManufacturer(manufacturer);
            response.put("success", true);
            response.put("message", "搜索药品成功");
            response.put("data", medicines);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "搜索药品失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取库存不足的药品
     */
    @GetMapping("/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockMedicines(@RequestParam(defaultValue = "10") Integer threshold) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Medicine> medicines = medicineService.getLowStockMedicines(threshold);
            response.put("success", true);
            response.put("message", "获取库存不足药品成功");
            response.put("data", medicines);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取库存不足药品失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取即将过期的药品
     */
    @GetMapping("/near-expiry")
    public ResponseEntity<Map<String, Object>> getNearExpiryMedicines() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Medicine> medicines = medicineService.getNearExpiryMedicines();
            response.put("success", true);
            response.put("message", "获取即将过期药品成功");
            response.put("data", medicines);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取即将过期药品失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 添加药品
     */
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addMedicine(@RequestBody Medicine medicine) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = medicineService.addMedicine(medicine);
            if (result) {
                response.put("success", true);
                response.put("message", "添加药品成功");
                response.put("data", medicine);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "添加药品失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加药品失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新药品
     */
    @PutMapping("/{medicineId}")
    public ResponseEntity<Map<String, Object>> updateMedicine(@PathVariable Integer medicineId, @RequestBody Medicine medicine) {
        Map<String, Object> response = new HashMap<>();
        try {
            medicine.setMedicineId(medicineId);
            boolean result = medicineService.updateMedicine(medicine);
            if (result) {
                response.put("success", true);
                response.put("message", "更新药品成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新药品失败，药品不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新药品失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新药品库存
     */
    @PutMapping("/{medicineId}/stock")
    public ResponseEntity<Map<String, Object>> updateMedicineStock(@PathVariable Integer medicineId, @RequestParam Integer stock) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = medicineService.updateMedicineStock(medicineId, stock);
            if (result) {
                response.put("success", true);
                response.put("message", "更新药品库存成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新药品库存失败，药品不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新药品库存失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除药品
     */
    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Map<String, Object>> deleteMedicine(@PathVariable Integer medicineId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = medicineService.deleteMedicine(medicineId);
            if (result) {
                response.put("success", true);
                response.put("message", "删除药品成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "删除药品失败，药品不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除药品失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取药品统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getMedicineStatistics() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 药品总数
            long totalCount = medicineService.countAllMedicines();
            statistics.put("totalCount", totalCount);
            
            // 库存总值
            Map<String, Object> stockValue = medicineService.countStockValue();
            statistics.put("stockValue", stockValue);
            
            response.put("success", true);
            response.put("message", "获取药品统计信息成功");
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取药品统计信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据处方ID获取处方药品列表
     */
    @GetMapping("/prescription/{prescriptionId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionMedicinesByPrescriptionId(@PathVariable Integer prescriptionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PrescriptionMedicine> prescriptionMedicines = prescriptionMedicineService.getPrescriptionMedicinesByPrescriptionId(prescriptionId);
            response.put("success", true);
            response.put("message", "获取处方药品列表成功");
            response.put("data", prescriptionMedicines);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取处方药品列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据药品ID获取处方药品列表
     */
    @GetMapping("/usage/{medicineId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionMedicinesByMedicineId(@PathVariable Integer medicineId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PrescriptionMedicine> prescriptionMedicines = prescriptionMedicineService.getPrescriptionMedicinesByMedicineId(medicineId);
            response.put("success", true);
            response.put("message", "获取处方药品列表成功");
            response.put("data", prescriptionMedicines);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取处方药品列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 