package com.example.shijiehouduan.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一API响应结果封装
 */
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static Result success() {
        return new Result(200, "操作成功");
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    /**
     * 成功返回结果
     *
     * @param message 提示信息
     * @param data    获取的数据
     */
    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    /**
     * 失败返回结果
     */
    public static Result error() {
        return new Result(500, "操作失败");
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static Result error(String message) {
        return new Result(500, message);
    }

    /**
     * 失败返回结果
     *
     * @param code    状态码
     * @param message 提示信息
     */
    public static Result error(Integer code, String message) {
        return new Result(code, message);
    }

    /**
     * 参数验证失败返回结果
     */
    public static Result validateFailed() {
        return new Result(400, "参数验证失败");
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static Result validateFailed(String message) {
        return new Result(400, message);
    }

    /**
     * 未登录返回结果
     */
    public static Result unauthorized() {
        return new Result(401, "暂未登录或token已经过期");
    }

    /**
     * 未授权返回结果
     */
    public static Result forbidden() {
        return new Result(403, "没有相关权限");
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