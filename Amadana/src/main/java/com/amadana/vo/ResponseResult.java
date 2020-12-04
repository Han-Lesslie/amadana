package com.amadana.vo;

import lombok.Data;

import java.util.List;

/**
 * 响应实体类
 * @param <T>
 */
@Data
public class ResponseResult<T> {

    private Integer code;
    private String message;
    private Integer count;
    private T data;
    private List<T> list;

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(Integer code, String message, Integer count, List<T> list) {
        this.code = code;
        this.message = message;
        this.count = count;
        this.list = list;
    }

}
