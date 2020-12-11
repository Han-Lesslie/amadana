package com.amadana.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 视频实体类
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    private Integer id;
    private String videoName;
    private String title;
    private String desc;
    private String videoImg;
    private String videoCover;
    private String videoPath;
    private Integer status;
    public String createTime;
    public String updateTime;
}
