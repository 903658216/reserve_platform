package com.jh.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期date的工具类
 */
public class DateUtil {


    /**
     * 获得当天之后next天的日期
     * @param next
     * @return
     */
    public static Date getNextDate(int next){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,next);
        return calendar.getTime();

    }

    /**
     * 计算入住的天数
     * @param beginTime 入住时间
     * @param endTime   离店时间
     * @return Integer 住了多少天
     */
    public static int getDates(Date beginTime,Date endTime ){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DDD");
        int start = Integer.parseInt(simpleDateFormat.format(beginTime));
        int end = Integer.parseInt(simpleDateFormat.format(endTime));

        return end-start;
    }


    /**
     * 将date日期转换成指定格式的字符串
     * @param date
     * @param pattern
     * @return
     */
    public static  String dateFormatToString(Date date,String pattern){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return  simpleDateFormat.format(date);
    }
}
