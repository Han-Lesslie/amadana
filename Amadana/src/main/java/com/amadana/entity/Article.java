package com.amadana.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  han
 * @date 2020/11/10
 * @desc 文章实体类
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Article implements Serializable {

    @ApiModelProperty(value = "文章ID",example = "1")
    private Integer id;
    private String author;
    private String title;
    private String createDate;
    private String updateDate;
    private String aritle_url;
    private String content;
    private Integer status;
}
