package com.ldz.obd.util;

import java.util.Date;

/**
 * auther chenwei
 * create at 2018/11/12
 */
public class DataUtil {

    public static Date parseDate(String s){
        if (s.length() < 6)return null;
        Date date = new Date();
        date.setYear(Integer.parseInt("20"+s.substring(4,6)));
        date.setMonth(Integer.parseInt(s.substring(2,4)));
        date.setDate(Integer.parseInt(s.substring(0,2)));
        date.setHours(Integer.parseInt(s.substring(6,8)));
        date.setMinutes(Integer.parseInt(s.substring(8,10)));
        date.setSeconds(Integer.parseInt(s.substring(10,12)));
        return date;
    }

    public static String[] parsePosition(String s){
        String latitude = s.substring(0,8);
        String longitude = s.substring(8,17);
        String[] position = new String[2];
        position[0] = latitude.substring(0,2)+"."+(Double.parseDouble(latitude.substring(2))/10000/60);
        position[1] = longitude.substring(0,3)+"."+(Double.parseDouble(longitude.substring(3,9))/10000/60);
        return position;
    }
}
