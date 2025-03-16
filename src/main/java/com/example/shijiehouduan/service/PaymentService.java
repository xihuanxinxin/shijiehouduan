package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Payment;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 支付记录服务接口
 */
public interface PaymentService {
    /**
     * 根据ID查询支付记录
     */
    Payment getPaymentById(Integer paymentId);

    /**
     * 根据患者ID查询支付记录
     */
    List<Payment> getPaymentsByPatientId(Integer patientId);

    /**
     * 根据订单类型和订单ID查询支付记录
     */
    List<Payment> getPaymentsByOrderTypeAndOrderId(String orderType, Integer orderId);

    /**
     * 根据支付状态查询支付记录
     */
    List<Payment> getPaymentsByStatus(String paymentStatus);

    /**
     * 根据支付方式查询支付记录
     */
    List<Payment> getPaymentsByPaymentMethod(String paymentMethod);

    /**
     * 根据时间范围查询支付记录
     */
    List<Payment> getPaymentsByTimeRange(Date startTime, Date endTime);

    /**
     * 查询所有支付记录
     */
    List<Payment> getAllPayments();

    /**
     * 分页查询支付记录
     */
    List<Payment> getPaymentsByPage(Integer pageNum, Integer pageSize);

    /**
     * 添加支付记录
     */
    boolean addPayment(Payment payment);

    /**
     * 更新支付记录
     */
    boolean updatePayment(Payment payment);

    /**
     * 更新支付状态
     */
    boolean updatePaymentStatus(Integer paymentId, String paymentStatus);

    /**
     * 删除支付记录
     */
    boolean deletePayment(Integer paymentId);

    /**
     * 统计支付记录总数
     */
    long countAllPayments();

    /**
     * 统计各支付状态的记录数
     */
    List<Map<String, Object>> countPaymentsByStatus();

    /**
     * 统计各支付方式的记录数
     */
    List<Map<String, Object>> countPaymentsByPaymentMethod();

    /**
     * 统计各订单类型的支付金额
     */
    List<Map<String, Object>> countAmountByOrderType();

    /**
     * 统计指定时间范围内的支付金额
     */
    Map<String, Object> countAmountByTimeRange(Date startTime, Date endTime);

    /**
     * 按日期统计支付金额
     */
    List<Map<String, Object>> countAmountByDate(Date startDate, Date endDate);

    /**
     * 按月份统计支付金额
     */
    List<Map<String, Object>> countAmountByMonth(Integer year);
} 