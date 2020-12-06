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
public class Banner implements Serializable {
    @ApiModelProperty(value = "图片ID",example = "1")
    private Integer id;
    /**图片名称*/
    private String bannerName;
    /**图片位置 banner*/
    private String bannerPosition;
    /**图片位置 存储位置*/
    private String bannerUrl;
    //图片名称
    private String imgName;
    /**链接类型*/
    private String linkeType;
    /**跳转链接*/
    private String jumpLink;
    /**链接类型*/
    private String linkWay;
    /**排序*/
    private Integer order;
    /**创建时间*/
    private String createTime;
    /**更新时间*/
    private String updateTime;
}
