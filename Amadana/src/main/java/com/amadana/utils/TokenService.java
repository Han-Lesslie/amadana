package com.amadana.utils;

import com.amadana.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @Author han
 * @Date 2019-12-17
 * 生成用户的登录的token
 */
public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);
    private static final Long EXPIRED_TIME = 86400000L;

    /**
     * 根据用户的账号和密码生成token
     * @param user
     * @return
     */
    public static String getToken(User user) {
        String token = "";
        token = JWT.create().withAudience(user.getAccount())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    /**
     * 根据当前时间与设置token初始时间比较，判断token是否过期
     * @param startTime
     * @param currentTime
     * @return
     */
    public static boolean isExpired(Long startTime,Long currentTime) {
        boolean expired = false;
        Long subTime = currentTime - startTime;
        if (subTime >= EXPIRED_TIME) {
            expired = true;
        }
        return expired;
    }

    /**
     * 刷新token的设置时间
     * @param startTime
     * @param token
     * @param httpSession
     */
    public static void refreshToken(Long startTime, String token, HttpSession httpSession) {
        httpSession.setAttribute("startTime",startTime);
        httpSession.setAttribute("token",token);
    }

    /**
     * 验证前端传过来的token是否有效，以及当前token是否过期
     * @param request
     * @param response
     * @param startTime
     * @param actual_token
     * @return
     */
    public static boolean validateToken(HttpServletRequest request, HttpServletResponse response,Long startTime,String actual_token) {
        boolean validation;
        String request_token = request.getHeader("token");
        LOGGER.info("request_token:{}",request_token);
        if (StringUtils.isEmpty(request_token) || !actual_token.equals(request_token)) {
            validation = false;
        }

        Long currentTime = System.currentTimeMillis();
        boolean isExpired = TokenService.isExpired(startTime,currentTime);
        if (isExpired) {
            validation = false;
        }else {
            TokenService.refreshToken(System.currentTimeMillis(),actual_token,request.getSession());
            validation = true;
        }
        return validation;
    }
}
