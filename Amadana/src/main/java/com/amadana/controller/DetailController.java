package com.amadana.controller;

import com.amadana.entity.Detail;
import com.amadana.enums.StateCode;
import com.amadana.service.DetailService;
import com.amadana.service.FileUploadService;
import com.amadana.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags = "图片")
//设置跨域请求源
@CrossOrigin(origins = "*", maxAge = 36000)
public class DetailController {
    @Autowired
    private DetailService detailService;
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseResult uploadPictture(@ApiParam("图片") @RequestParam(value = "file", required = false) MultipartFile file) {

        String name = file.getOriginalFilename();
        try {
            Map<String, Object> result = fileUploadService.uploadPicture(file);
            if ((int)result.get("code") == 200) {
                String url = (String) result.get("path");
                Detail detail = new Detail();
                detail.setName(name);
                detail.setUrl(url);
                boolean b = detailService.saveDetail(detail);
                if (b) {
                    return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),result);
                }
                return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }else {
                return new ResponseResult(StateCode.FAILED.getCode(), (String) result.get("msg"));
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(StateCode.OPERATION_ERROR.getCode(),StateCode.OPERATION_ERROR.getMessage());
        }
        /*InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = file.getInputStream();
            data = new byte[file.getInputStream().available()];
            in.read(data);
            in.close();
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            Detail detail = new Detail();
            detail.setName(name);
            detail.setUrl("data:image/jpg;base64," + encoder.encode(data));
            boolean success = detailService.saveDetail(detail);
            if (success) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),success);
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage(),success);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseResult(StateCode.OPERATION_ERROR.getCode(),StateCode.OPERATION_ERROR.getMessage());
        }*/

    }

    @GetMapping("/getDetailByName")
    public ResponseResult getDetailByName(@RequestParam("name") String name, HttpServletRequest request) {
        return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),detailService.findDetailByName(name));
    }
    @GetMapping("/getDetails")
    public ResponseResult getDetails() {
        return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),detailService.getDetails());
    }
}
