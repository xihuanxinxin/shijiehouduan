package com.example.shijiehouduan.service.impl;

import com.example.shijiehouduan.dao.BedDao;
import com.example.shijiehouduan.dao.HospitalizationDao;
import com.example.shijiehouduan.entity.Bed;
import com.example.shijiehouduan.entity.Hospitalization;
import com.example.shijiehouduan.service.HospitalizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 住院记录服务实现类
 */
@Service
public class HospitalizationServiceImpl implements HospitalizationService {

    @Autowired
    private HospitalizationDao hospitalizationDao;
    
    @Autowired
    private BedDao bedDao;

    @Override
    public Hospitalization getHospitalizationById(Integer hospitalizationId) {
        return hospitalizationDao.findById(hospitalizationId);
    }

    @Override
    public List<Hospitalization> getHospitalizationsByPatientId(Integer patientId) {
        return hospitalizationDao.findByPatientId(patientId);
    }

    @Override
    public List<Hospitalization> getHospitalizationsByDoctorId(Integer doctorId) {
        return hospitalizationDao.findByDoctorId(doctorId);
    }

    @Override
    public Hospitalization getHospitalizationByBedId(Integer bedId) {
        return hospitalizationDao.findByBedId(bedId);
    }

    @Override
    public List<Hospitalization> getCurrentHospitalizations() {
        return hospitalizationDao.findCurrentHospitalizations();
    }
    
    @Override
    @Transactional
    public boolean addHospitalization(Hospitalization hospitalization) {
        // 设置默认状态为在院
        if (hospitalization.getStatus() == null) {
            hospitalization.setStatus("在院");
        }
        
        // 设置创建时间
        if (hospitalization.getCreatedAt() == null) {
            hospitalization.setCreatedAt(new Date());
        }
        
        // 更新床位状态为占用，并设置当前患者ID
        Bed bed = bedDao.findById(hospitalization.getBedId());
        if (bed != null && "空闲".equals(bed.getStatus())) {
            bed.setStatus("占用");
            bed.setCurrentPatientId(hospitalization.getPatientId());
            bedDao.update(bed);
        } else {
            // 如果床位不存在或不是空闲状态，则不能添加住院记录
            return false;
        }
        
        return hospitalizationDao.insert(hospitalization) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateHospitalization(Hospitalization hospitalization) {
        // 查询原住院记录信息
        Hospitalization originalHospitalization = hospitalizationDao.findById(hospitalization.getHospitalizationId());
        if (originalHospitalization == null) {
            return false;
        }

        // 如果未提供bedId，则使用原记录中的bedId
        if (hospitalization.getBedId() == null) {
            hospitalization.setBedId(originalHospitalization.getBedId());
        }
        
        // 如果修改了床位，需要更新床位状态
        if (!hospitalization.getBedId().equals(originalHospitalization.getBedId())) {
            // 原床位恢复为空闲状态
            Bed originalBed = bedDao.findById(originalHospitalization.getBedId());
            if (originalBed != null) {
                originalBed.setStatus("空闲");
                originalBed.setCurrentPatientId(null);
                bedDao.update(originalBed);
            }
            
            // 新床位设置为占用状态
            Bed newBed = bedDao.findById(hospitalization.getBedId());
            if (newBed != null && "空闲".equals(newBed.getStatus())) {
                newBed.setStatus("占用");
                newBed.setCurrentPatientId(originalHospitalization.getPatientId());
                bedDao.update(newBed);
            } else {
                // 如果新床位不存在或不是空闲状态，则不能更新住院记录
                return false;
            }
        }
        
        // 确保patientId和创建时间不变
        hospitalization.setPatientId(originalHospitalization.getPatientId());
        hospitalization.setCreatedAt(originalHospitalization.getCreatedAt());
        
        return hospitalizationDao.update(hospitalization) > 0;
    }
    
    @Override
    @Transactional
    public boolean setHospitalizationInHospital(Integer hospitalizationId) {
        return hospitalizationDao.updateStatus(hospitalizationId, "在院") > 0;
    }
    
    @Override
    @Transactional
    public boolean setHospitalizationDischarged(Integer hospitalizationId, Date dischargeDate) {
        // 更新出院日期
        hospitalizationDao.updateDischargeDate(hospitalizationId, dischargeDate);
        
        // 更新状态为出院
        boolean success = hospitalizationDao.updateStatus(hospitalizationId, "出院") > 0;
        
        if (success) {
            // 释放床位
            Hospitalization hospitalization = hospitalizationDao.findById(hospitalizationId);
            if (hospitalization != null) {
                Bed bed = bedDao.findById(hospitalization.getBedId());
                if (bed != null) {
                    bed.setStatus("空闲");
                    bed.setCurrentPatientId(null);
                    bedDao.update(bed);
                }
            }
        }
        
        return success;
    }
    
    @Override
    @Transactional
    public boolean setHospitalizationTransferred(Integer hospitalizationId, Date dischargeDate) {
        // 更新出院日期
        hospitalizationDao.updateDischargeDate(hospitalizationId, dischargeDate);
        
        // 更新状态为转院
        boolean success = hospitalizationDao.updateStatus(hospitalizationId, "转院") > 0;
        
        if (success) {
            // 释放床位
            Hospitalization hospitalization = hospitalizationDao.findById(hospitalizationId);
            if (hospitalization != null) {
                Bed bed = bedDao.findById(hospitalization.getBedId());
                if (bed != null) {
                    bed.setStatus("空闲");
                    bed.setCurrentPatientId(null);
                    bedDao.update(bed);
                }
            }
        }
        
        return success;
    }
    
    @Override
    @Transactional
    public boolean setHospitalizationDeceased(Integer hospitalizationId, Date dischargeDate) {
        // 更新出院日期
        hospitalizationDao.updateDischargeDate(hospitalizationId, dischargeDate);
        
        // 更新状态为死亡
        boolean success = hospitalizationDao.updateStatus(hospitalizationId, "死亡") > 0;
        
        if (success) {
            // 释放床位
            Hospitalization hospitalization = hospitalizationDao.findById(hospitalizationId);
            if (hospitalization != null) {
                Bed bed = bedDao.findById(hospitalization.getBedId());
                if (bed != null) {
                    bed.setStatus("空闲");
                    bed.setCurrentPatientId(null);
                    bedDao.update(bed);
                }
            }
        }
        
        return success;
    }
    
    @Override
    public List<Hospitalization> getHospitalizationsByStatus(String status) {
        return hospitalizationDao.findByStatus(status);
    }
} 