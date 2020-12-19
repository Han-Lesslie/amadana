package com.amadana.controller;

import com.amadana.annotation.UserLoginToken;
import com.amadana.constant.Constant;
import com.amadana.entity.Banner;
import com.amadana.enums.StateCode;
import com.amadana.result.Expection;
import com.amadana.service.BannerService;
import com.amadana.service.FileUploadService;
import com.amadana.utils.RedisUtils;
import com.amadana.vo.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(tags = "图片管理接口")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 36000)
public class BannerController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BannerController.class);

    @Autowired
    private BannerService bannerService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation("添加图片")
    @PostMapping("/saveBanner")
    @UserLoginToken
    public ResponseResult saveBanner(@ApiParam("图片信息") @RequestBody Banner banner, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            // 刷新token的缓存时间
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            Expection expection = bannerService.saveBanner(banner);

            if (expection.getCode() == Constant.SUCCESS_CODE) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());

        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    /*@ApiOperation("图片分页")
    @GetMapping("/getBanner")
    @UserLoginToken
    public ResponseResult getPage(@ApiParam("当前页")@RequestParam("currentPage") int currentPage, @ApiParam("页数")@RequestParam("pageSize")int pageSize,
                                  HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            PageInfo pageInfo = bannerService.findByPage(currentPage,pageSize);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),
                    (int) pageInfo.getTotal(),pageInfo.getList());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }*/


    @ApiOperation("图片分页")
    @PostMapping("/getBanner")
    @UserLoginToken
    public ResponseResult getPage(@RequestBody Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            PageInfo pageInfo = bannerService.findByPage(map);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),
                    (int) pageInfo.getTotal(),pageInfo.getList());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    @ApiOperation("删除图片")
    @GetMapping("deleteBannerById")
    @UserLoginToken
    public ResponseResult delete(@ApiParam("图片ID")@RequestParam("id")Integer id, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            boolean state = bannerService.delete(id);
            if (state) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    @ApiOperation("图片搜索与分页")
    @GetMapping("/searchImg")
    @UserLoginToken
    public ResponseResult searchByImgPosition(@ApiParam("当前页面")@RequestParam("currentPage")int currentPage,@ApiParam("页数")@RequestParam("pageSize") int pageSize,
                                      @ApiParam("图片位置")@RequestParam("imgPosition") String imgPosition,HttpServletRequest request,HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            PageInfo pageInfo = bannerService.searchByImgPosition(currentPage,pageSize,imgPosition);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),
                    (int) pageInfo.getTotal(),pageInfo.getList());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    @ApiOperation("根据id获取图片")
    @GetMapping("/findBannerById")
    @UserLoginToken
    public ResponseResult findBannerById(@ApiParam("ID")@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            Banner banner = bannerService.findBannerById(id);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),
                    banner);
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    @ApiOperation("更新图片")
    @PostMapping("/updateBanner")
    @UserLoginToken
    public ResponseResult updateBanner(@ApiParam("图片内容")@RequestBody Banner banner, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);

            // 更新图片信息
            boolean flag = bannerService.update(banner);
            if (flag) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());

        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    @ApiOperation("广告位轮播图")
    @GetMapping("/getBanners")

    public ResponseResult getBanners(HttpServletRequest request, HttpServletResponse response) {

        return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(), bannerService.getBanners());

    }

    private boolean isExpire(String token) {
        String value = (String) redisUtils.get(token);
        return value == null ? true : false;
    }
}
