package com.amadana.dao;

import com.amadana.entity.Banner;

import java.util.List;
import java.util.Map;

public interface BannerMapper extends BaseMapper {

    List<Banner> searchByImgPosition(String ImgPosition);
    Banner findBannerById(Integer id);
    List<Banner> getBanners();
    List<Banner> search(Map<String,Object> map);
}
