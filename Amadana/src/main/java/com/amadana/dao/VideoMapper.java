package com.amadana.dao;

import com.amadana.entity.Video;

import java.util.List;

/**
 * 视频接口
 */
public interface VideoMapper extends BaseMapper{

    List<Video> getVideos();

    Video getVideoById(Integer id);
}
