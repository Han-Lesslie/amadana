package com.amadana.dao;

import com.amadana.entity.Banner;

import java.util.List;

public interface BannerMapper extends BaseMapper {

    List<Banner> searchByImgPosition(String ImgPosition);
    Banner findBannerById(Integer id);
}
