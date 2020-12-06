package com.amadana.utils;

/**
 * 判空工具类
 */
public class CommonUtil {

    public static boolean isNull(String str) {
        if (null == str && "".equals(str)){
            return true;
        }
        return false;
    }
}
