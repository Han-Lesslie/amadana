package com.amadana;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ParseException {
        String beginTime = "2020-12-12 00:00:00";
        String endTime = "2030-12-14 00:00:00";
        String bTime = beginTime.split(" ")[0];
        String eTime = endTime.split(" ")[0];

        Temporal temporal = LocalDate.parse(bTime);
        Temporal temporal1 = LocalDate.parse(eTime);
        long sub = ChronoUnit.MONTHS.between(temporal, temporal1);
        System.out.println(sub);

        List<String> years = processToDate(beginTime,endTime,false);
        years.forEach(i->{
            System.out.println(i);
        });
    }

    protected static String dateFormat = "yyyy-MM-dd hh:mm:ss";
    protected static SimpleDateFormat format = new SimpleDateFormat(dateFormat);

    public static List<String> processToDate(final String startDate, final String endDate,boolean flag) throws ParseException {

        List<String> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(startDate));
        // 转换成月
        if (flag){
            String start = startDate.substring(0,startDate.lastIndexOf("-"));
            String end = endDate.substring(0,endDate.lastIndexOf("-"));

            if (start.equals(end)) {
                dateList.add(start.concat("-01 00:00:00"));
                return dateList;
            }

            dateList.add(start.concat("-01 00:00:00"));

            while (calendar.getTime().before(format.parse(endDate))) {
                calendar.add(Calendar.MONTH,1);
                String date = format.format(calendar.getTime());
                dateList.add(date.substring(0,date.lastIndexOf("-")).concat("-01 00:00:00"));
            }
            dateList.remove(dateList.size()-1);
            return dateList;
        }else {
            String start = startDate.substring(0,startDate.indexOf("-"));
            String end = endDate.substring(0,endDate.indexOf("-"));
            if (start.equals(end)) {
                dateList.add(startDate.concat("-01-01: 00:00:00"));
                return dateList;
            }

            dateList.add(start.concat("-01-01 00:00:00"));
            while (calendar.getTime().before(format.parse(endDate))) {
                calendar.add(Calendar.YEAR,1);
                String date = format.format(calendar.getTime());
                dateList.add(date.substring(0,date.indexOf("-")).concat("-01-01 00:00:00"));
            }
            dateList.remove(dateList.size()-1);
            return dateList;
        }

    }


    private static Date str2Date(String str) {
        if (str == null) return null;
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
