package com.amadana.utils;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HttpSessionUtils {
    private  static  HttpSession httpSession;

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return requestAttributes == null ?null:requestAttributes.getRequest();
    }
    public static HttpSession getHttpSession(HttpServletRequest request, HttpServletResponse response) {
        if (httpSession == null) {
            httpSession = request.getSession();
        }
        return httpSession;
    }
}
