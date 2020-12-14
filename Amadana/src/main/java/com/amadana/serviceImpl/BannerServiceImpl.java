package com.amadana.serviceImpl;

import com.amadana.dao.BannerMapper;
import com.amadana.dao.DetailMapper;
import com.amadana.entity.Banner;
import com.amadana.enums.StateCode;
import com.amadana.result.Expection;
import com.amadana.service.BannerService;
import com.amadana.service.FileUploadService;
import com.amadana.utils.DateFormat;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private FileUploadService fileUploadService;

    private final static Logger LOGGER = LoggerFactory.getLogger(BannerServiceImpl.class);
    @Override
    public Expection saveBanner(Banner banner) {
        if (banner == null) {
            return new Expection(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }

        int count = 0;
        try {
            if (banner.getId() != null) {
                count = bannerMapper.update(banner);
            }else {
                count = bannerMapper.save(banner);
            }
            if (count > 0) {
                return new Expection(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new Expection(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new Expection(StateCode.OPERATION_ERROR.getCode(),StateCode.OPERATION_ERROR.getMessage());
        }
    }

    @Override
    public PageInfo findByPage(int currentPage, int pageSize) {
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 6 : pageSize;
        PageHelper.startPage(currentPage,pageSize);
        List<Banner> bannerList = bannerMapper.findAll();
        PageInfo pageInfo = new PageInfo(bannerList);
        return  pageInfo;
    }



    @Override
    public boolean delete(Integer id) {
        if (null == id) {
            return false;
        }
        try {
            // 删除banner对应的文件
            String fileName = bannerMapper.findBannerById(id).getImgName();
            fileUploadService.deteleFile(fileName);
            int count = bannerMapper.delete(id);
            return count <= 0 ? false : true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PageInfo searchByImgPosition(int currentPage, int pageSize, String imgPosition) {
        if (StringUtils.isEmpty(imgPosition) || StringUtils.isBlank(imgPosition)) {
            return null;
        }
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 6 : pageSize;
        PageHelper.startPage(currentPage,pageSize);
        List<Banner> banners = bannerMapper.searchByImgPosition(imgPosition);
        PageInfo pageInfo = new PageInfo<>(banners);
        return pageInfo;
    }

    @Override
    public boolean update(Banner banner) {
        if (banner == null || banner.getId() == null) {
            return false;
        }
        try {
            banner.setUpdateTime(DateFormat.dateFormat(new Date()));
            int count = bannerMapper.update(banner);
            return count > 0 ? true : false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Banner findBannerById(Integer id) {
        if(id == null) {
            return null;
        }
        try {
            Banner banner = bannerMapper.findBannerById(id);
            return banner;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Banner> getBanners() {
        List<Banner> banners = null;
        try{
            banners = bannerMapper.getBanners();
        }catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return (null == banners || 0 == banners.size()) ? new ArrayList<>() : banners;
    }
}
