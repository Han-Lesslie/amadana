package com.amadana.controller;

import com.amadana.enums.StateCode;
import com.amadana.utils.HttpSessionUtils;
import com.amadana.vo.ResponseResult;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Api(tags = "验证码接口")
@RestController
@RequestMapping("/api")
//设置跨域请求源
@CrossOrigin(origins = "*", maxAge = 3600)
public class KaptchaController {
    private final Logger LOGGER = LoggerFactory.getLogger(KaptchaController.class);
//    @Autowired
//    private JavaMailSender javaMailSender;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    private static HttpSession httpSession;

    public static void getHttpSession(HttpServletRequest request,HttpServletResponse response) {
        httpSession = HttpSessionUtils.getHttpSession(request,response);
    }

    /**
     * 生成图片验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation("生成验证码")
    @GetMapping("/createImageCode")
    public void createImageCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
        byte[] captchaChangeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生成验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            LOGGER.info("createText:{}",createText);
            httpSession = request.getSession();
            httpSession.setAttribute("imageCode",createText);
            //使用生成的验证码字符串返回一个BufferedImage对象并转为byte写入byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge,"jpg",jpegOutputStream);
            LOGGER.info("createImageCode:{}",request.getSession().getAttribute("imageCode"));
        }catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChangeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChangeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    /**
     * 验证验证码
     * @param code
     * @return
     */
    @ApiOperation("校验验证码")
    @GetMapping("/validate")
    @ResponseBody
    public ResponseResult validate(@ApiParam("验证码") @RequestParam("checkCode") String code) {

        LOGGER.info("checkCode:{}",code);
        // 从session中获取验证码
        String imageCode = (String) httpSession.getAttribute("imageCode");
        LOGGER.info("imageCode:{}", imageCode);
        if (imageCode != null && !"".equals(imageCode)) {
            boolean flag = imageCode.equals(code);
            if (flag) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new ResponseResult(StateCode.INVAILDATE.getCode(),StateCode.INVAILDATE.getMessage());
            }
        }
        return null;
    }

    /**
     * 用户名申诉成功下一步，根据邮箱进行申诉，验证成功后将发送修改密码的方法至用户邮箱
     * @param receiver
     * @param code
     * @return
     */
    /*@GetMapping("/appeal")
    @ResponseBody
    public Object appeal(@RequestParam("email") String receiver,@RequestParam("checkCode") String code) {
        JSONObject jsonObject = new JSONObject();
        LOGGER.info("email:{}",receiver);
        if (StringUtils.isEmpty(receiver) || StringUtils.isBlank(receiver)) {
            jsonObject.put("code",1023);
            jsonObject.put("message","邮箱不能为空!");
            return jsonObject;
        }

        if (StringUtils.isBlank(code) || StringUtils.isEmpty(code)) {
            jsonObject.put("code",1023);
            jsonObject.put("message","验证码不能为空!");
            return jsonObject;
        }

        String imageCode = (String) httpSession.getAttribute("imageCode");
        if (code.equals(imageCode)) {
            jsonObject.put("code", StateCode.SUCCESS.getCode());
            jsonObject.put("message","申诉成功，请查收邮箱!");

            MimeMessage message = javaMailSender.createMimeMessage();
            try{
                //true表示需要创建一个multipart message
                MimeMessageHelper helper = new MimeMessageHelper(message,true);
                String sender = Constant.SENDER;
                helper.setFrom(sender);
                helper.setTo(receiver);
                helper.setSubject(Constant.SUBJECT);
                helper.setText(Constant.CONTENT,true);

                javaMailSender.send(message);
                LOGGER.info("发送邮件成功");
                return jsonObject;

            }catch (MessagingException e) {
                e.printStackTrace();
                LOGGER.error("发送邮件时发生异常！",e);
                jsonObject.put("code",StateCode.OPERATION_ERROR.getCode());
                jsonObject.put("message",e.getMessage());
                return jsonObject;
            }
        }else {
            jsonObject.put("code",1023);
            jsonObject.put("message","验证码错误!");
            return jsonObject;
        }
    }
*/
}
