package com.amadana.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    public static String dateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = formatter.format(date);
        return  result;
    }
}
