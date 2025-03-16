package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.Doctor;
import com.example.shijiehouduan.entity.Bed;
import com.example.shijiehouduan.service.DoctorService;
import com.example.shijiehouduan.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医疗资源管理控制器
 */
@RestController
@RequestMapping("/api/admin/resources")
public class MedicalResourceController {

    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private BedService bedService;

    /**
     * 获取所有医生列表
     */
    @GetMapping("/doctors")
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            response.put("success", true);
            response.put("message", "获取医生列表成功");
            response.put("data", doctors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取医生列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据ID获取医生
     */
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable Integer doctorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Doctor doctor = doctorService.getDoctorById(doctorId);
            if (doctor != null) {
                response.put("success", true);
                response.put("message", "获取医生成功");
                response.put("data", doctor);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "医生不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取医生失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据专业查询医生
     */
    @GetMapping("/doctors/specialty")
    public ResponseEntity<Map<String, Object>> getDoctorsBySpecialty(@RequestParam String specialty) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Doctor> doctors = doctorService.getDoctorsBySpecialty(specialty);
            response.put("success", true);
            response.put("message", "获取医生列表成功");
            response.put("data", doctors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取医生列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 添加医生
     */
    @PostMapping("/doctors")
    public ResponseEntity<Map<String, Object>> addDoctor(@RequestBody Doctor doctor) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 设置创建时间
            doctor.setCreatedAt(new Date());
            
            boolean result = doctorService.addDoctor(doctor);
            if (result) {
                response.put("success", true);
                response.put("message", "添加医生成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "添加医生失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加医生失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新医生
     */
    @PutMapping("/doctors/{doctorId}")
    public ResponseEntity<Map<String, Object>> updateDoctor(@PathVariable Integer doctorId, @RequestBody Doctor doctor) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 设置医生ID
            doctor.setDoctorId(doctorId);
            
            boolean result = doctorService.updateDoctor(doctor);
            if (result) {
                response.put("success", true);
                response.put("message", "更新医生成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新医生失败，医生不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新医生失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除医生
     */
    @DeleteMapping("/doctors/{doctorId}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(@PathVariable Integer doctorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = doctorService.deleteDoctor(doctorId);
            if (result) {
                response.put("success", true);
                response.put("message", "删除医生成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "删除医生失败，医生不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除医生失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取所有床位列表
     */
    @GetMapping("/beds")
    public ResponseEntity<Map<String, Object>> getAllBeds() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Bed> beds = bedService.getAllBeds();
            response.put("success", true);
            response.put("message", "获取床位列表成功");
            response.put("data", beds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取床位列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据ID获取床位
     */
    @GetMapping("/beds/{bedId}")
    public ResponseEntity<Map<String, Object>> getBedById(@PathVariable Integer bedId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Bed bed = bedService.getBedById(bedId);
            if (bed != null) {
                response.put("success", true);
                response.put("message", "获取床位成功");
                response.put("data", bed);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "床位不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取床位失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据状态查询床位
     */
    @GetMapping("/beds/status")
    public ResponseEntity<Map<String, Object>> getBedsByStatus(@RequestParam String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Bed> beds = bedService.getBedsByStatus(status);
            response.put("success", true);
            response.put("message", "获取床位列表成功");
            response.put("data", beds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取床位列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据房间号查询床位
     */
    @GetMapping("/beds/room")
    public ResponseEntity<Map<String, Object>> getBedsByRoomNumber(@RequestParam String roomNumber) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Bed> beds = bedService.getBedsByRoomNumber(roomNumber);
            response.put("success", true);
            response.put("message", "获取床位列表成功");
            response.put("data", beds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取床位列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 添加床位
     */
    @PostMapping("/beds")
    public ResponseEntity<Map<String, Object>> addBed(@RequestBody Bed bed) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 设置默认状态为空闲
            if (bed.getStatus() == null) {
                bed.setStatus("空闲");
            }
            
            boolean result = bedService.addBed(bed);
            if (result) {
                response.put("success", true);
                response.put("message", "添加床位成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "添加床位失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加床位失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新床位
     */
    @PutMapping("/beds/{bedId}")
    public ResponseEntity<Map<String, Object>> updateBed(@PathVariable Integer bedId, @RequestBody Bed bed) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 设置床位ID
            bed.setBedId(bedId);
            
            boolean result = bedService.updateBed(bed);
            if (result) {
                response.put("success", true);
                response.put("message", "更新床位成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新床位失败，床位不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新床位失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新床位状态
     */
    @PutMapping("/beds/{bedId}/status")
    public ResponseEntity<Map<String, Object>> updateBedStatus(@PathVariable Integer bedId, @RequestParam String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = bedService.updateBedStatus(bedId, status);
            if (result) {
                response.put("success", true);
                response.put("message", "更新床位状态成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新床位状态失败，床位不存在");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新床位状态失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除床位
     */
    @DeleteMapping("/beds/{bedId}")
    public ResponseEntity<Map<String, Object>> deleteBed(@PathVariable Integer bedId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = bedService.deleteBed(bedId);
            if (result) {
                response.put("success", true);
                response.put("message", "删除床位成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "删除床位失败，床位不存在或正在使用中");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除床位失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 