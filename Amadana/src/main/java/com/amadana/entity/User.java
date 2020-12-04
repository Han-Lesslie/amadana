package com.amadana.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * @ author Han
 * @ date 2020/11/9
 * @ desc 用户实体类
 */
public class User implements Serializable {
    @ApiModelProperty(value = "用户ID",example = "1")
    private Integer id;
    private String account;
    private String password;
    private Integer role;
    private Integer status;
    private Date createTime;
    private Date  updateTime;
}
