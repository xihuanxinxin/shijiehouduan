package com.example.shijiehouduan.service;

import com.example.shijiehouduan.entity.Bed;
import java.util.List;

/**
 * 床位服务接口
 */
public interface BedService {
    /**
     * 根据ID查询床位
     */
    Bed getBedById(Integer bedId);

    /**
     * 查询所有床位
     */
    List<Bed> getAllBeds();

    /**
     * 根据状态查询床位
     */
    List<Bed> getBedsByStatus(String status);

    /**
     * 根据房间号查询床位
     */
    List<Bed> getBedsByRoomNumber(String roomNumber);

    /**
     * 根据当前患者ID查询床位
     */
    Bed getBedByCurrentPatientId(Integer patientId);

    /**
     * 新增床位
     */
    boolean addBed(Bed bed);

    /**
     * 更新床位
     */
    boolean updateBed(Bed bed);

    /**
     * 更新床位状态
     */
    boolean updateBedStatus(Integer bedId, String status);

    /**
     * 分配床位给患者
     */
    boolean assignBedToPatient(Integer bedId, Integer patientId);

    /**
     * 释放床位
     */
    boolean releaseBed(Integer bedId);

    /**
     * 删除床位
     */
    boolean deleteBed(Integer bedId);
} 