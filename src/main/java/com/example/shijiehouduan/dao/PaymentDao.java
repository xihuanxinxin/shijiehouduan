package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 支付记录数据访问接口
 */
@Mapper
public interface PaymentDao {
    /**
     * 根据ID查询支付记录
     * @param paymentId 支付记录ID
     * @return 支付记录
     */
    Payment findById(Integer paymentId);

    /**
     * 根据患者ID查询支付记录
     * @param patientId 患者ID
     * @return 支付记录列表
     */
    List<Payment> findByPatientId(Integer patientId);

    /**
     * 根据订单类型和订单ID查询支付记录
     * @param orderType 订单类型
     * @param orderId 订单ID
     * @return 支付记录列表
     */
    List<Payment> findByOrderTypeAndOrderId(@Param("orderType") String orderType, @Param("orderId") Integer orderId);

    /**
     * 根据支付状态查询支付记录
     * @param paymentStatus 支付状态
     * @return 支付记录列表
     */
    List<Payment> findByStatus(@Param("paymentStatus") String paymentStatus);

    /**
     * 根据支付方式查询支付记录
     * @param paymentMethod 支付方式
     * @return 支付记录列表
     */
    List<Payment> findByPaymentMethod(@Param("paymentMethod") String paymentMethod);

    /**
     * 根据时间范围查询支付记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 支付记录列表
     */
    List<Payment> findByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 查询所有支付记录
     * @return 支付记录列表
     */
    List<Payment> findAll();

    /**
     * 分页查询支付记录
     * @param offset 偏移量
     * @param limit 每页记录数
     * @return 支付记录列表
     */
    List<Payment> findByPage(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 新增支付记录
     * @param payment 支付记录
     * @return 影响行数
     */
    int insert(Payment payment);

    /**
     * 更新支付记录
     * @param payment 支付记录
     * @return 影响行数
     */
    int update(Payment payment);

    /**
     * 更新支付状态
     * @param paymentId 支付记录ID
     * @param paymentStatus 支付状态
     * @return 影响行数
     */
    int updateStatus(@Param("paymentId") Integer paymentId, @Param("paymentStatus") String paymentStatus);

    /**
     * 删除支付记录
     * @param paymentId 支付记录ID
     * @return 影响行数
     */
    int delete(Integer paymentId);

    /**
     * 统计支付记录总数
     * @return 记录总数
     */
    long countAll();

    /**
     * 统计各支付状态的记录数
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 统计各支付方式的记录数
     * @return 统计结果
     */
    List<Map<String, Object>> countByPaymentMethod();

    /**
     * 统计各订单类型的支付金额
     * @return 统计结果
     */
    List<Map<String, Object>> countAmountByOrderType();

    /**
     * 统计指定时间范围内的支付金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> countAmountByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 按日期统计支付金额
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> countAmountByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 按月份统计支付金额
     * @param year 年份
     * @return 统计结果
     */
    List<Map<String, Object>> countAmountByMonth(@Param("year") Integer year);
} 