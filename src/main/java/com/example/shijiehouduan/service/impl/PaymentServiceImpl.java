package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.PaymentDao;
import com.example.shijiehouduan.entity.Payment;
import com.example.shijiehouduan.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 支付记录服务实现类
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Override
    public Payment getPaymentById(Integer paymentId) {
        return paymentDao.findById(paymentId);
    }

    @Override
    public List<Payment> getPaymentsByPatientId(Integer patientId) {
        return paymentDao.findByPatientId(patientId);
    }

    @Override
    public List<Payment> getPaymentsByOrderTypeAndOrderId(String orderType, Integer orderId) {
        return paymentDao.findByOrderTypeAndOrderId(orderType, orderId);
    }

    @Override
    public List<Payment> getPaymentsByStatus(String paymentStatus) {
        return paymentDao.findByStatus(paymentStatus);
    }

    @Override
    public List<Payment> getPaymentsByPaymentMethod(String paymentMethod) {
        return paymentDao.findByPaymentMethod(paymentMethod);
    }

    @Override
    public List<Payment> getPaymentsByTimeRange(Date startTime, Date endTime) {
        return paymentDao.findByTimeRange(startTime, endTime);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDao.findAll();
    }

    @Override
    public List<Payment> getPaymentsByPage(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return paymentDao.findByPage(offset, pageSize);
    }

    @Override
    @Transactional
    public boolean addPayment(Payment payment) {
        // 设置创建时间
        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(new Date());
        }
        
        // 设置支付时间（如果是已支付状态）
        if ("已支付".equals(payment.getPaymentStatus()) && payment.getPaymentTime() == null) {
            payment.setPaymentTime(new Date());
        }
        
        return paymentDao.insert(payment) > 0;
    }

    @Override
    @Transactional
    public boolean updatePayment(Payment payment) {
        // 检查支付记录是否存在
        Payment existingPayment = paymentDao.findById(payment.getPaymentId());
        if (existingPayment == null) {
            return false;
        }
        
        // 如果状态从非已支付变为已支付，设置支付时间
        if (!"已支付".equals(existingPayment.getPaymentStatus()) && 
            "已支付".equals(payment.getPaymentStatus()) && 
            payment.getPaymentTime() == null) {
            payment.setPaymentTime(new Date());
        }
        
        return paymentDao.update(payment) > 0;
    }

    @Override
    @Transactional
    public boolean updatePaymentStatus(Integer paymentId, String paymentStatus) {
        // 检查支付记录是否存在
        Payment existingPayment = paymentDao.findById(paymentId);
        if (existingPayment == null) {
            return false;
        }
        
        // 检查状态是否有效
        if (!isValidStatus(paymentStatus)) {
            return false;
        }
        
        return paymentDao.updateStatus(paymentId, paymentStatus) > 0;
    }

    @Override
    @Transactional
    public boolean deletePayment(Integer paymentId) {
        // 检查支付记录是否存在
        Payment existingPayment = paymentDao.findById(paymentId);
        if (existingPayment == null) {
            return false;
        }
        
        return paymentDao.delete(paymentId) > 0;
    }

    @Override
    public long countAllPayments() {
        return paymentDao.countAll();
    }

    @Override
    public List<Map<String, Object>> countPaymentsByStatus() {
        return paymentDao.countByStatus();
    }

    @Override
    public List<Map<String, Object>> countPaymentsByPaymentMethod() {
        return paymentDao.countByPaymentMethod();
    }

    @Override
    public List<Map<String, Object>> countAmountByOrderType() {
        return paymentDao.countAmountByOrderType();
    }

    @Override
    public Map<String, Object> countAmountByTimeRange(Date startTime, Date endTime) {
        return paymentDao.countAmountByTimeRange(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> countAmountByDate(Date startDate, Date endDate) {
        return paymentDao.countAmountByDate(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countAmountByMonth(Integer year) {
        return paymentDao.countAmountByMonth(year);
    }
    
    /**
     * 检查支付状态是否有效
     */
    private boolean isValidStatus(String status) {
        return "待支付".equals(status) || "已支付".equals(status) || 
               "已退款".equals(status) || "部分退款".equals(status);
    }
} 