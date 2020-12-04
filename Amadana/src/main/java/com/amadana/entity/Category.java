package com.amadana.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category implements Serializable {

    @ApiModelProperty(value = "产品分类ID",example = "1")
    private Integer id;
    private String categoryName;
    private Integer status;
    private Integer order;
    private String createTime;
    private String updateTime;
}
