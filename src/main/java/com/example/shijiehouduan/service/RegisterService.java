package com.example.shijiehouduan.service;

import com.example.shijiehouduan.common.Result;
import com.example.shijiehouduan.dto.RegisterRequest;

/**
 * 注册服务接口
 */
public interface RegisterService {
    /**
     * 患者注册
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    Result registerPatient(RegisterRequest registerRequest);
} 