package com.amadana.service;

import com.amadana.entity.Video;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 视频接口类
 */
public interface VideoService {
    boolean saveVideo(Video video);
    PageInfo listVideo(int currentPage, int pageSize);
    List<Video> getVideos();
    boolean update(Video video);
    boolean deleteById(Integer id);
}
