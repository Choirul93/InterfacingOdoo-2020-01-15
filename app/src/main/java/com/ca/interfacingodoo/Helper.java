package com.ca.interfacingodoo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Helper {

    //String date = Helper.getTimeStamp("yyyy-MM-dd HH:mm:ss");
    public static String getTimeStamp(String pattern){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return String.valueOf(sdf.format(timestamp));

    }
}
