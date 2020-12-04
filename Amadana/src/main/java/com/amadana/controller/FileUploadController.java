package com.amadana.controller;

import com.amadana.constant.Constant;
import com.amadana.enums.StateCode;
import com.amadana.service.FileUploadService;
import com.amadana.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "图片上传接口")
@Controller
@RequestMapping("/api")
public class FileUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("上传图片")
    @RequestMapping(value = "/setFileUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult setFileUpload(@ApiParam("图片") @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            Map<String, Object> resultMap = upload(file);
            if (Constant.SUCCESS_CODE == (int)resultMap.get("code")) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),resultMap.get("path"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(">>>>>>图片上传异常，e={}", e.getMessage());
        }
        return null;
    }

    private Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            if (!file.isEmpty()) {
                Map<String, Object> picMap = fileUploadService.uploadPicture(file);
                if (Constant.SUCCESS_CODE == (int)picMap.get("code")){
                    return picMap;
                } else {
                    returnMap.put("result", "上传失败！");
                    returnMap.put("msg", picMap.get("result"));
                }
            } else {
                LOGGER.info(">>>>>>上传图片为空文件");
                returnMap.put("result", "上传失败");
                returnMap.put("msg", "文件为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }

}

