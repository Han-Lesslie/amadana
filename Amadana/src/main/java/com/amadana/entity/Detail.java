package com.amadana.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 图片详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Detail {

    private Integer id;
    private String name;
    private String url;
}
