package com.amadana.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    private static String encode(String string) {
        if (string != null && string != "") {
            string = string.trim();
        }
        StringBuffer sb = new StringBuffer();
        try {
            //加密对象，指定加密方法
            MessageDigest md5 = MessageDigest.getInstance("md5");
            //准备要加密的数据
            byte[] bytes = string.getBytes();
            //加密
            byte [] digests = md5.digest(bytes);

            char [] chars = hexArray();
            //处理成16进制的字符串
            for(byte b: digests) {
                sb.append(chars[(b>>4)&15]);
                sb.append(chars[b&15]);
            }

        }catch (NoSuchAlgorithmException e) {
            System.err.println("have no such algorithm");
        }
        return sb.toString();
    }

    private final static char[] hexArray() {
        char [] chars = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        return chars;
    }

    public static String getEncodeChar(String str) {
        return encode(str);
    }

    public static void main(String[] args) {
        String password = getEncodeChar("zsw123456");
        System.out.println(password);

    }
}
