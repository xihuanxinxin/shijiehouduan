package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.Payment;
import com.example.shijiehouduan.entity.User;
import com.example.shijiehouduan.service.PaymentService;
import com.example.shijiehouduan.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付记录控制器
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 根据ID查询支付记录
     * @param paymentId 支付记录ID
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/{paymentId}")
    public Result getPaymentById(@PathVariable Integer paymentId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            if (payment != null) {
                // 检查权限：患者只能查看自己的支付记录
                if (isPatient(request) && !payment.getPatientId().equals(getPatientIdFromUser(currentUser))) {
                    return Result.forbidden();
                }
                return Result.success(payment);
            } else {
                return Result.error("支付记录不存在");
            }
        } catch (Exception e) {
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID查询支付记录
     * @param patientId 患者ID
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/patient/{patientId}")
    public Result getPaymentsByPatientId(@PathVariable Integer patientId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 检查权限：患者只能查看自己的支付记录
        if (isPatient(request) && !patientId.equals(getPatientIdFromUser(currentUser))) {
            return Result.forbidden();
        }
        
        try {
            List<Payment> payments = paymentService.getPaymentsByPatientId(patientId);
            return Result.success(payments);
        } catch (Exception e) {
            return Result.error("查询患者支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据订单类型和订单ID查询支付记录
     * @param orderType 订单类型
     * @param orderId 订单ID
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/order")
    public Result getPaymentsByOrderTypeAndOrderId(
            @RequestParam String orderType,
            @RequestParam Integer orderId,
            HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        try {
            List<Payment> payments = paymentService.getPaymentsByOrderTypeAndOrderId(orderType, orderId);
            
            // 检查权限：患者只能查看自己的支付记录
            if (isPatient(request)) {
                Integer patientId = getPatientIdFromUser(currentUser);
                payments.removeIf(payment -> !payment.getPatientId().equals(patientId));
            }
            
            return Result.success(payments);
        } catch (Exception e) {
            return Result.error("查询订单支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据支付状态查询支付记录
     * @param status 支付状态
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/status/{status}")
    public Result getPaymentsByStatus(@PathVariable String status, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看所有状态的记录
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Payment> payments = paymentService.getPaymentsByStatus(status);
            return Result.success(payments);
        } catch (Exception e) {
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据支付方式查询支付记录
     * @param method 支付方式
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/method/{method}")
    public Result getPaymentsByPaymentMethod(@PathVariable String method, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看所有支付方式的记录
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Payment> payments = paymentService.getPaymentsByPaymentMethod(method);
            return Result.success(payments);
        } catch (Exception e) {
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据时间范围查询支付记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/time-range")
    public Result getPaymentsByTimeRange(
            @RequestParam Date startTime,
            @RequestParam Date endTime,
            HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看所有时间范围的记录
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Payment> payments = paymentService.getPaymentsByTimeRange(startTime, endTime);
            return Result.success(payments);
        } catch (Exception e) {
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有支付记录
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/all")
    public Result getAllPayments(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看所有记录
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Payment> payments = paymentService.getAllPayments();
            return Result.success(payments);
        } catch (Exception e) {
            return Result.error("查询所有支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询支付记录
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/page")
    public Result getPaymentsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        try {
            List<Payment> payments;
            long total;
            
            // 根据角色获取不同的数据
            if (isPatient(request)) {
                // 患者只能查看自己的支付记录
                Integer patientId = getPatientIdFromUser(currentUser);
                payments = paymentService.getPaymentsByPage(pageNum, pageSize);
                payments.removeIf(payment -> !payment.getPatientId().equals(patientId));
                total = paymentService.countAllPayments();
            } else {
                // 管理员和医生可以查看所有记录
                payments = paymentService.getPaymentsByPage(pageNum, pageSize);
                total = paymentService.countAllPayments();
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", payments);
            data.put("total", total);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("分页查询支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 新增支付记录
     * @param payment 支付记录
     * @param request HTTP请求
     * @return 结果
     */
    @PostMapping
    public Result addPayment(@RequestBody Payment payment, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 患者只能为自己添加支付记录
        if (isPatient(request)) {
            Integer patientId = getPatientIdFromUser(currentUser);
            if (!payment.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
        }
        
        try {
            boolean success = paymentService.addPayment(payment);
            if (success) {
                return Result.success("新增支付记录成功", payment);
            } else {
                return Result.error("新增支付记录失败");
            }
        } catch (Exception e) {
            return Result.error("新增支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 更新支付记录
     * @param payment 支付记录
     * @param request HTTP请求
     * @return 结果
     */
    @PutMapping
    public Result updatePayment(@RequestBody Payment payment, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 检查权限
        if (isPatient(request)) {
            // 患者只能更新自己的支付记录
            Integer patientId = getPatientIdFromUser(currentUser);
            Payment existingPayment = paymentService.getPaymentById(payment.getPaymentId());
            if (existingPayment == null || !existingPayment.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
        }
        
        try {
            boolean success = paymentService.updatePayment(payment);
            if (success) {
                return Result.success("更新支付记录成功");
            } else {
                return Result.error("更新支付记录失败");
            }
        } catch (Exception e) {
            return Result.error("更新支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 更新支付状态
     * @param paymentId 支付记录ID
     * @param status 支付状态
     * @param request HTTP请求
     * @return 结果
     */
    @PutMapping("/{paymentId}/status")
    public Result updatePaymentStatus(
            @PathVariable Integer paymentId,
            @RequestParam String status,
            HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 检查权限
        if (isPatient(request)) {
            // 患者只能更新自己的支付记录状态
            Integer patientId = getPatientIdFromUser(currentUser);
            Payment existingPayment = paymentService.getPaymentById(paymentId);
            if (existingPayment == null || !existingPayment.getPatientId().equals(patientId)) {
                return Result.forbidden();
            }
        } else if (!isAdmin(request) && !isDoctor(request)) {
            // 只有患者、医生和管理员可以更新支付状态
            return Result.forbidden();
        }
        
        try {
            boolean success = paymentService.updatePaymentStatus(paymentId, status);
            if (success) {
                return Result.success("更新支付状态成功");
            } else {
                return Result.error("更新支付状态失败");
            }
        } catch (Exception e) {
            return Result.error("更新支付状态失败: " + e.getMessage());
        }
    }

    /**
     * 删除支付记录
     * @param paymentId 支付记录ID
     * @param request HTTP请求
     * @return 结果
     */
    @DeleteMapping("/{paymentId}")
    public Result deletePayment(@PathVariable Integer paymentId, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 检查权限：只有管理员可以删除支付记录
        if (!isAdmin(request)) {
            return Result.forbidden();
        }
        
        try {
            boolean success = paymentService.deletePayment(paymentId);
            if (success) {
                return Result.success("删除支付记录成功");
            } else {
                return Result.error("删除支付记录失败");
            }
        } catch (Exception e) {
            return Result.error("删除支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 统计支付记录总数
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/count")
    public Result countAllPayments(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看统计数据
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            long count = paymentService.countAllPayments();
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 统计各支付状态的记录数
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/count/status")
    public Result countPaymentsByStatus(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看统计数据
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Map<String, Object>> data = paymentService.countPaymentsByStatus();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计支付状态失败: " + e.getMessage());
        }
    }

    /**
     * 统计各支付方式的记录数
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/count/method")
    public Result countPaymentsByPaymentMethod(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看统计数据
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Map<String, Object>> data = paymentService.countPaymentsByPaymentMethod();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计支付方式失败: " + e.getMessage());
        }
    }

    /**
     * 统计各订单类型的支付金额
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/count/order-type")
    public Result countAmountByOrderType(HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看统计数据
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Map<String, Object>> data = paymentService.countAmountByOrderType();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计订单类型支付金额失败: " + e.getMessage());
        }
    }

    /**
     * 统计指定时间范围内的支付金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/count/time-range")
    public Result countAmountByTimeRange(
            @RequestParam Date startTime,
            @RequestParam Date endTime,
            HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看统计数据
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            Map<String, Object> data = paymentService.countAmountByTimeRange(startTime, endTime);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计时间范围支付金额失败: " + e.getMessage());
        }
    }

    /**
     * 按日期统计支付金额
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/count/date")
    public Result countAmountByDate(
            @RequestParam Date startDate,
            @RequestParam Date endDate,
            HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看统计数据
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Map<String, Object>> data = paymentService.countAmountByDate(startDate, endDate);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("按日期统计支付金额失败: " + e.getMessage());
        }
    }

    /**
     * 按月份统计支付金额
     * @param year 年份
     * @param request HTTP请求
     * @return 结果
     */
    @GetMapping("/count/month")
    public Result countAmountByMonth(@RequestParam Integer year, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 管理员或医生才能查看统计数据
        if (!isAdmin(request) && !isDoctor(request)) {
            return Result.forbidden();
        }
        
        try {
            List<Map<String, Object>> data = paymentService.countAmountByMonth(year);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("按月份统计支付金额失败: " + e.getMessage());
        }
    }
    
    /**
     * 从用户获取患者ID
     * @param user 用户
     * @return 患者ID
     */
    private Integer getPatientIdFromUser(User user) {
        // 假设患者的patientId与userId相关联，实际项目中应该通过服务层获取
        // 这里需要根据实际情况修改实现方法
        return user.getUserId();
    }
} 