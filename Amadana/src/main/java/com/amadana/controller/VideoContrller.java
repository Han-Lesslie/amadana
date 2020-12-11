package com.amadana.controller;

import com.amadana.annotation.UserLoginToken;
import com.amadana.constant.Constant;
import com.amadana.entity.Video;
import com.amadana.enums.StateCode;
import com.amadana.service.VideoService;
import com.amadana.utils.RedisUtils;
import com.amadana.utils.TokenService;
import com.amadana.vo.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Api(tags = "视频接口")
//设置跨域请求源
@CrossOrigin(origins = "*", maxAge = 36000)
public class VideoContrller {
    @Autowired
    private VideoService videoService;
    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "uploadVideo", method = RequestMethod.POST)
    public Map<String,String> saveVideo(@RequestParam("file") MultipartFile file)
            throws IllegalStateException {
        Map<String,String> resultMap = new HashMap<>();
        try{
            //获取文件后缀
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                    .toLowerCase();
            // 重构文件名称
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            String newVidoeName = pikId + "." + fileExt;
            //保存视频
            File fileSave = new File("C:\\Users\\LENOVO\\Desktop\\amadana官网\\project\\static\\video", newVidoeName);
            file.transferTo(fileSave);
            resultMap.put("resCode","1");
            resultMap.put("webShowPath","C:\\Users\\LENOVO\\Desktop\\amadana官网\\project\\static\\video" + newVidoeName);

            return  resultMap;

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("resCode","0");
            return  resultMap ;

        }
    }


    @ApiOperation("上传视频信息")
    @PostMapping("/insertVideo")
    @UserLoginToken
    public ResponseResult insertVideoInfo(@ApiParam("视频信息") @RequestBody Video video, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        // 判断token是否过期
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            try {
                redisUtils.setExpire(token,token, Constant.EXPIRE_TIME);
                boolean flag = videoService.saveVideo(video);
                if (flag) {
                    return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
                }else {
                    return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
                }
            }catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult(StateCode.OPERATION_ERROR.getCode(),StateCode.OPERATION_ERROR.getMessage());
            }
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    /**
     * 获取分页
     * @param currentPage
     * @param pageSize
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("视频分页")
    @GetMapping("/getVideos")
    @UserLoginToken
    public ResponseResult getArticlePage(@ApiParam("当前页")@RequestParam("currentPage") int currentPage, @ApiParam("页数")@RequestParam("pageSize") int pageSize,
                                         HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            PageInfo pageInfo = videoService.listVideo(currentPage,pageSize);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage()
                    , (int) pageInfo.getTotal(),pageInfo.getList());
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    /**
     * 获取前三个视频列表
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("获取前三个视频列表")
    @GetMapping("/findVideos")
    public ResponseResult getVideos(HttpServletRequest request,HttpServletResponse response) {
        return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),videoService.getVideos());
    }

    /**
     * 更新视频
     * @param video
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("更新视频")
    @PostMapping("/updateVideo")
    @UserLoginToken
    public ResponseResult getArticlePage(@ApiParam("请求体")@RequestBody Video video, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            boolean isSuccss = videoService.update(video);
            if (isSuccss) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }


    /**
     * 更新视频
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("删除视频")
    @GetMapping("/deleteVideoById")
    @UserLoginToken
    public ResponseResult getArticlePage(@ApiParam("ID")Integer id, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            boolean isSuccss = videoService.deleteById(id);
            if (isSuccss) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    private boolean isExpire(String token) {
        String value = (String) redisUtils.get(token);
        return value == null ? true : false;
    }
}
