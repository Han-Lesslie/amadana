package com.amadana.serviceImpl;

import com.amadana.dao.BannerMapper;
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
import org.springframework.boot.logging.LoggerGroup;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

        if (null == banner.getBannerUrl() || "".equals(banner.getBannerUrl())) {
            return new Expection(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }else {
            //将字符串分成数组
            String[] d = banner.getBannerUrl().split("base64,");
            if(d == null || d.length != 2) {
                return new Expection(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }
        }
        LOGGER.info("banner:{}",banner);
        banner.setCreateTime(DateFormat.dateFormat(new Date()));
        int count = 0;
        try {
            count = bannerMapper.save(banner);
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
            //banner.setBannerUrl((String) fileUploadService.getPath().get("path"));
            banner.setUpdateTime(DateFormat.dateFormat(new Date()));
            int count = bannerMapper.update(banner);
            return count > 0 ? true : false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
