package com.amadana.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.amadana.entity.User;
import com.amadana.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.amadana.annotation.PassToken;
import com.amadana.annotation.UserLoginToken;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author WANGYINGBO
 * @Date 2019-12-17
 *拦截器获取token并验证token
 *
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(HandlerInterceptor.class);
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LOGGER.info("开始拦截请求.............................");
        String  servletPath = request.getServletPath();
        LOGGER.info("请求方法 ============ >>>> {}",servletPath);
        log.info(request.getRemoteAddr());
        //从http请求头中取出token
        String token = request.getHeader("token");
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注解，有则跳过验证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                //执行认证
                if (token == null) {
                    //request.getRequestDispatcher("/api/login").forward(request, response);
                    response.sendRedirect("/login");

                    return false;//throw new RuntimeException("无token，请重新登录");
                }
                //获取token中的teacherName
                String account;
                try {
                    account = JWT.decode(token).getAudience().get(0);
                }catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
                List<User> users = userService.findUserByAccount(account);
                if (users.get(0) == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                //验证token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(users.get(0).getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                }catch (JWTVerificationException j) {
                    throw new RuntimeException("401");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
