package com.amadana.enums;

/**
 * @Author han
 * @Date 2019-12-16
 * @desc 状态码，主要用于记录用户操作状态
 *
 */
public enum StateCode {
    SUCCESS(200,"操作成功"),
    FAILED(1023,"操作失败"),
    LOGIN_SUCCESS(200,"登录成功"),
    LOGIN_FAILED(1000,"用户名或密码错误"),
    REGISTRY_SUCCESS(200,"注册成功"),
    REGISTRY_FAILED(1000,"注册失败"),
    DUPLICATE_REGISTRY(1001,"用户已存在"),
    INVALID_USER(1002,"用户不存在"),
    REOURCE_NOT_FOUND(404,"找不到指定资源"),
    REQUIRED_FAILED(400,"请求错误"),
    SERVER_FAILED(500,"服务端异常"),
    INTERNET_ERROR(1024,"网络异常"),
    OPERATION_ERROR(1025,"操作异常"),
    UNAUTHORIZED(405,"未授权"),
    TIMEOUT(408,"请求超时"),
    INVAILDATE(403,"验证码错误");
    private int code;
    private String message;
    StateCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void  setMessage(String message) {
        this.message = message;
    }
}
