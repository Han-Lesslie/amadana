package com.amadana.controller;

import com.amadana.constant.Constant;
import com.amadana.entity.User;
import com.amadana.enums.StateCode;
import com.amadana.result.Expection;
import com.amadana.service.UserService;
import com.amadana.utils.RedisUtils;
import com.amadana.utils.TokenService;
import com.amadana.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
//设置跨域请求源
@CrossOrigin(origins = "*", maxAge = 36000)
@Api(tags = "用户登录注销相关接口")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtils redisUtils;


    /**
     * 登录API，对前端传入的用户数据做验证，验证成功后，httpSession设置token以及生成token时间
     * 返回token以及相关状态码至前端
     * @param user
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("登录接口")
    @PostMapping("/login")
    public ResponseResult login(@ApiParam("用户账号密码") @RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws SQLException {

        Expection expection = null;
        try {
             expection = userService.login(user);
            if (expection.getCode() == Constant.SUCCESS_CODE) {
                // 登录成功，获取Token
                String token = TokenService.getToken(user);
                redisUtils.setExpire(token,token, Constant.EXPIRE_TIME);
                return new ResponseResult(expection.getCode(),expection.getMsg(),token);
            }
            return new ResponseResult(expection.getCode(),expection.getMsg());
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(StateCode.INTERNET_ERROR.getCode(),StateCode.INTERNET_ERROR.getMessage());
        }
    }

    /**
     * 用户注销功能，在注销前，验证token的有效性，注销成功后，设置httpSession属性
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("注销接口")
    @GetMapping("/loginout")
    @ResponseBody
    public ResponseResult loginout(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        String value = (String) redisUtils.get(token);
        // 判断token是否过期
        boolean isExpire = value == null ?  false : true;

        if (isExpire) {
            redisUtils.setExpire(token,null,0);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());

        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

}
