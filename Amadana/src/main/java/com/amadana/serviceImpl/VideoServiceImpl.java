package com.amadana.serviceImpl;

import com.amadana.dao.VideoMapper;
import com.amadana.entity.Article;
import com.amadana.entity.Video;
import com.amadana.service.VideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;
    @Override
    public boolean saveVideo(Video video) {
        if (null == video) {
            return false;
        }
        try{
            int count = videoMapper.save(video);
            return count == 0 ? false : true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PageInfo listVideo(int currentPage, int pageSize) {
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 6 : pageSize;
        PageHelper.startPage(currentPage,pageSize);
        List<Article> articleList = videoMapper.findAll();
        // 分页
        PageInfo pageInfo = new PageInfo(articleList);
        return pageInfo;
    }

    @Override
    public List<Video> getVideos() {
        List<Video> videos = null;
        try{
            videos = videoMapper.getVideos();
        }catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return (null == videos || 0 == videos.size()) ? new ArrayList<>() : videos;
    }

    @Override
    public boolean update(Video video) {
        if (null == video || null == video.getId()){
            return false;
        }
        try {
            int count = videoMapper.update(video);
            return count == 0 ? false:true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        if (null == id){
            return false;
        }
        try {
            int count = videoMapper.delete(id);
            return count == 0 ? false:true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
