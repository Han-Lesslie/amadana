package com.amadana.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 产品详情实体表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetail implements Serializable {

    @ApiModelProperty(value = "产品详情ID",example = "1")
    private Integer id;
    /**
     * 详情图片
     */
    private String detailImg;
    /**
     * 图片名称
     */
    private String imgName;
    /**
     * 图片描述
     */
    private String imgDesc;
    /**
     * 详情链接
     */
    private String detailLink;
    /**
     * 产品详情状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 产品详情所对应的产品，多对一
     */
    private Product product;

}
