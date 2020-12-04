package com.amadana.result;

/**
 * @Author wWANGYINGBO
 * @Date 2019-12-16
 * @用户操作状态的封装类，将此返回给前端
 */
public class Expection<T> {
    private Integer code;
    private String msg;
    private T t;
    public Expection() {
    }

    public Expection(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Expection(Integer code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
