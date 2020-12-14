package com.amadana.service;

import com.amadana.entity.Banner;
import com.amadana.result.Expection;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * banner接口
 */
public interface BannerService {
    Expection saveBanner(Banner banner);
    PageInfo findByPage(int currentPage,int pageSize);
    boolean delete(Integer id);
    PageInfo searchByImgPosition(int currentPage,int pageSize,String imgPosition);
    boolean update(Banner banner);
    Banner findBannerById(Integer id);
    List<Banner> getBanners();
}
