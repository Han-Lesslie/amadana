package com.amadana.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 产品实体表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product implements Serializable {

    @ApiModelProperty(value = "产品ID",example = "1")
    private Integer id;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品描述
     */
    private String productDesc;
    /**
     * 产品Icon
     */
    private String productIcon;
    /**
     * 产品图片
     */
    private String productImg;
    /**
     * 产品展示图片
     */
    private String displayImg;
    /**
     * 产品价格
     */
    private double price;
    /**
     * 产品编号
     */
    private String productNumber;
    /**
     * 产品型号
     */
    private String productModel;
    /**
     * 产品状态
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
     * 产品对应的分类，多对一
     */
    private Category category;
    /**
     * 产品详情，一对多
     */
    private List<ProductDetail> productDetails;
}
