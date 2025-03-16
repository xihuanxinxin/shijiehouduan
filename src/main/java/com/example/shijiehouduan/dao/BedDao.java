package com.example.shijiehouduan.dao;

import com.example.shijiehouduan.entity.Bed;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 床位DAO接口
 */
public interface BedDao {
    /**
     * 根据ID查询床位
     */
    Bed findById(Integer bedId);

    /**
     * 查询所有床位
     */
    List<Bed> findAll();

    /**
     * 根据状态查询床位
     */
    List<Bed> findByStatus(String status);

    /**
     * 根据房间号查询床位
     */
    List<Bed> findByRoomNumber(String roomNumber);

    /**
     * 根据当前患者ID查询床位
     */
    Bed findByCurrentPatientId(Integer patientId);

    /**
     * 新增床位
     */
    int insert(Bed bed);

    /**
     * 更新床位
     */
    int update(Bed bed);

    /**
     * 更新床位状态
     */
    int updateStatus(@Param("bedId") Integer bedId, @Param("status") String status);

    /**
     * 分配床位给患者
     */
    int assignToPatient(@Param("bedId") Integer bedId, @Param("patientId") Integer patientId);

    /**
     * 释放床位
     */
    int release(Integer bedId);

    /**
     * 删除床位
     */
    int delete(Integer bedId);
} 