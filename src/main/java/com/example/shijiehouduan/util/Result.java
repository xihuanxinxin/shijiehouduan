package com.example.shijiehouduan.util;

import java.io.Serializable;

/**
 * 统一API返回结果封装
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    /**
     * 成功
     */
    public static Result success() {
        return success("操作成功");
    }

    /**
     * 成功
     * @param message 消息
     */
    public static Result success(String message) {
        return success(message, null);
    }

    /**
     * 成功
     * @param data 数据
     */
    public static Result success(Object data) {
        return success("操作成功", data);
    }

    /**
     * 成功
     * @param message 消息
     * @param data 数据
     */
    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    /**
     * 失败
     */
    public static Result error() {
        return error("操作失败");
    }

    /**
     * 失败
     * @param message 消息
     */
    public static Result error(String message) {
        return error(500, message);
    }

    /**
     * 失败
     * @param code 状态码
     * @param message 消息
     */
    public static Result error(Integer code, String message) {
        return new Result(code, message, null);
    }

    /**
     * 构造方法
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     */
    private Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
} 