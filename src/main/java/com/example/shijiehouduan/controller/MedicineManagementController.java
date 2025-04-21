package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.entity.Medicine;
import com.example.shijiehouduan.entity.PrescriptionMedicine;
import com.example.shijiehouduan.service.MedicineService;
import com.example.shijiehouduan.service.PrescriptionMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药品耗材管理控制器
 */
@RestController
@RequestMapping("/api/admin/medicines")
public class MedicineManagementController extends BaseController {

    @Autowired
    private MedicineService medicineService;
    
    @Autowired
    private PrescriptionMedicineService prescriptionMedicineService;

    /**
     * 获取所有药品
     */
    @GetMapping("")
    public Result getAllMedicines(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Medicine> medicines = medicineService.getAllMedicines();
            return Result.success(medicines);
        } catch (Exception e) {
            return Result.error("获取药品列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取药品
     */
    @GetMapping("/{medicineId}")
    public Result getMedicineById(@PathVariable Integer medicineId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            Medicine medicine = medicineService.getMedicineById(medicineId);
            if (medicine != null) {
                return Result.success(medicine);
            } else {
                return Result.error("药品不存在");
            }
        } catch (Exception e) {
            return Result.error("获取药品失败: " + e.getMessage());
        }
    }

    /**
     * 根据名称搜索药品
     */
    @GetMapping("/search")
    public Result searchMedicinesByName(@RequestParam String name, HttpServletRequest request) {
        // 验证是否为管理员或医生
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Medicine> medicines = medicineService.getMedicinesByName(name);
            return Result.success(medicines);
        } catch (Exception e) {
            return Result.error("搜索药品失败: " + e.getMessage());
        }
    }

    /**
     * 根据生产厂家搜索药品
     */
    @GetMapping("/manufacturer")
    public Result searchMedicinesByManufacturer(@RequestParam String manufacturer, HttpServletRequest request) {
        // 验证是否为管理员或医生
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Medicine> medicines = medicineService.getMedicinesByManufacturer(manufacturer);
            return Result.success(medicines);
        } catch (Exception e) {
            return Result.error("搜索药品失败: " + e.getMessage());
        }
    }

    /**
     * 获取库存不足的药品
     */
    @GetMapping("/low-stock")
    public Result getLowStockMedicines(@RequestParam(defaultValue = "10") Integer threshold, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Medicine> medicines = medicineService.getLowStockMedicines(threshold);
            return Result.success(medicines);
        } catch (Exception e) {
            return Result.error("获取库存不足药品失败: " + e.getMessage());
        }
    }

    /**
     * 获取即将过期的药品
     */
    @GetMapping("/near-expiry")
    public Result getNearExpiryMedicines(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Medicine> medicines = medicineService.getNearExpiryMedicines();
            return Result.success(medicines);
        } catch (Exception e) {
            return Result.error("获取即将过期药品失败: " + e.getMessage());
        }
    }

    /**
     * 添加药品
     */
    @PostMapping("")
    public Result addMedicine(@RequestBody Medicine medicine, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            boolean result = medicineService.addMedicine(medicine);
            if (result) {
                return Result.success(medicine);
            } else {
                return Result.error("添加药品失败");
            }
        } catch (Exception e) {
            return Result.error("添加药品失败: " + e.getMessage());
        }
    }

    /**
     * 更新药品
     */
    @PutMapping("/{medicineId}")
    public Result updateMedicine(@PathVariable Integer medicineId, @RequestBody Medicine medicine, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            medicine.setMedicineId(medicineId);
            boolean result = medicineService.updateMedicine(medicine);
            if (result) {
                return Result.success("更新药品成功");
            } else {
                return Result.error("更新药品失败，药品不存在");
            }
        } catch (Exception e) {
            return Result.error("更新药品失败: " + e.getMessage());
        }
    }

    /**
     * 更新药品库存
     */
    @PutMapping("/{medicineId}/stock")
    public Result updateMedicineStock(@PathVariable Integer medicineId, @RequestParam Integer stock, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            boolean result = medicineService.updateMedicineStock(medicineId, stock);
            if (result) {
                return Result.success("更新药品库存成功");
            } else {
                return Result.error("更新药品库存失败，药品不存在");
            }
        } catch (Exception e) {
            return Result.error("更新药品库存失败: " + e.getMessage());
        }
    }

    /**
     * 删除药品
     */
    @DeleteMapping("/{medicineId}")
    public Result deleteMedicine(@PathVariable Integer medicineId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            boolean result = medicineService.deleteMedicine(medicineId);
            if (result) {
                return Result.success("删除药品成功");
            } else {
                return Result.error("删除药品失败，药品不存在");
            }
        } catch (Exception e) {
            return Result.error("删除药品失败: " + e.getMessage());
        }
    }

    /**
     * 获取药品统计信息
     */
    @GetMapping("/statistics")
    public Result getMedicineStatistics(HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 药品总数
            long totalCount = medicineService.countAllMedicines();
            statistics.put("totalCount", totalCount);
            
            // 库存总值
            Map<String, Object> stockValue = medicineService.countStockValue();
            statistics.put("stockValue", stockValue);
            
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取药品统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据处方ID获取处方药品列表
     */
    @GetMapping("/prescription/{prescriptionId}")
    public Result getPrescriptionMedicinesByPrescriptionId(@PathVariable Integer prescriptionId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<PrescriptionMedicine> prescriptionMedicines = prescriptionMedicineService.getPrescriptionMedicinesByPrescriptionId(prescriptionId);
            return Result.success(prescriptionMedicines);
        } catch (Exception e) {
            return Result.error("获取处方药品列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据药品ID获取处方药品列表
     */
    @GetMapping("/usage/{medicineId}")
    public Result getPrescriptionMedicinesByMedicineId(@PathVariable Integer medicineId, HttpServletRequest request) {
        // 验证是否为管理员
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            List<PrescriptionMedicine> prescriptionMedicines = prescriptionMedicineService.getPrescriptionMedicinesByMedicineId(medicineId);
            return Result.success(prescriptionMedicines);
        } catch (Exception e) {
            return Result.error("获取处方药品列表失败: " + e.getMessage());
        }
    }
} 