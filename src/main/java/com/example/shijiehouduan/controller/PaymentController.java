package com.example.shijiehouduan.controller;

import com.example.shijiehouduan.entity.Payment;
import com.example.shijiehouduan.service.PaymentService;
import com.example.shijiehouduan.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付记录控制器
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 根据ID查询支付记录
     * @param paymentId 支付记录ID
     * @return 结果
     */
    @GetMapping("/{paymentId}")
    public Result getPaymentById(@PathVariable Integer paymentId) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            if (payment != null) {
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
     * @return 结果
     */
    @GetMapping("/patient/{patientId}")
    public Result getPaymentsByPatientId(@PathVariable Integer patientId) {
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
     * @return 结果
     */
    @GetMapping("/order")
    public Result getPaymentsByOrderTypeAndOrderId(
            @RequestParam String orderType,
            @RequestParam Integer orderId) {
        try {
            List<Payment> payments = paymentService.getPaymentsByOrderTypeAndOrderId(orderType, orderId);
            return Result.success(payments);
        } catch (Exception e) {
            return Result.error("查询订单支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据支付状态查询支付记录
     * @param status 支付状态
     * @return 结果
     */
    @GetMapping("/status/{status}")
    public Result getPaymentsByStatus(@PathVariable String status) {
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
     * @return 结果
     */
    @GetMapping("/method/{method}")
    public Result getPaymentsByPaymentMethod(@PathVariable String method) {
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
     * @return 结果
     */
    @GetMapping("/time-range")
    public Result getPaymentsByTimeRange(
            @RequestParam Date startTime,
            @RequestParam Date endTime) {
        try {
            List<Payment> payments = paymentService.getPaymentsByTimeRange(startTime, endTime);
            return Result.success(payments);
        } catch (Exception e) {
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有支付记录
     * @return 结果
     */
    @GetMapping("/all")
    public Result getAllPayments() {
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
     * @return 结果
     */
    @GetMapping("/page")
    public Result getPaymentsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            List<Payment> payments = paymentService.getPaymentsByPage(pageNum, pageSize);
            long total = paymentService.countAllPayments();
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
     * @return 结果
     */
    @PostMapping
    public Result addPayment(@RequestBody Payment payment) {
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
     * @return 结果
     */
    @PutMapping
    public Result updatePayment(@RequestBody Payment payment) {
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
     * @return 结果
     */
    @PutMapping("/{paymentId}/status")
    public Result updatePaymentStatus(
            @PathVariable Integer paymentId,
            @RequestParam String status) {
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
     * @return 结果
     */
    @DeleteMapping("/{paymentId}")
    public Result deletePayment(@PathVariable Integer paymentId) {
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
     * @return 结果
     */
    @GetMapping("/count")
    public Result countAllPayments() {
        try {
            long count = paymentService.countAllPayments();
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计支付记录失败: " + e.getMessage());
        }
    }

    /**
     * 统计各支付状态的记录数
     * @return 结果
     */
    @GetMapping("/count/status")
    public Result countPaymentsByStatus() {
        try {
            List<Map<String, Object>> data = paymentService.countPaymentsByStatus();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计支付状态失败: " + e.getMessage());
        }
    }

    /**
     * 统计各支付方式的记录数
     * @return 结果
     */
    @GetMapping("/count/method")
    public Result countPaymentsByPaymentMethod() {
        try {
            List<Map<String, Object>> data = paymentService.countPaymentsByPaymentMethod();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("统计支付方式失败: " + e.getMessage());
        }
    }

    /**
     * 统计各订单类型的支付金额
     * @return 结果
     */
    @GetMapping("/count/order-type")
    public Result countAmountByOrderType() {
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
     * @return 结果
     */
    @GetMapping("/count/time-range")
    public Result countAmountByTimeRange(
            @RequestParam Date startTime,
            @RequestParam Date endTime) {
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
     * @return 结果
     */
    @GetMapping("/count/date")
    public Result countAmountByDate(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
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
     * @return 结果
     */
    @GetMapping("/count/month")
    public Result countAmountByMonth(@RequestParam Integer year) {
        try {
            List<Map<String, Object>> data = paymentService.countAmountByMonth(year);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("按月份统计支付金额失败: " + e.getMessage());
        }
    }
} 