package com.example.biddingservice.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author lukewwang
 * @time 2020/11/25 5:33 PM
 */
public class TimeHandler {

    /**
     * 将时间戳{@code timestamp}转换成作为{@code Date}类型数据。
     *
     * @param timestamp 时间戳
     * @return 比较日期用的{@code Date}类型数据
     */
    public static Date toDate(String timestamp) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date startDate = null;
        try {
            startDate = fmt.parse(timestamp);
        } catch (ParseException e) {
            throw new UnsupportedOperationException();
        }
        return startDate;
    }

    /**
     * 获取监控开始前的延迟时长，单位为毫秒
     *
     * @param startTime 监控任务开始日期时间
     * @param currTime 系统当前日期时间
     * @return 用于 ScheduleExecutorService 的 {@code Long} 时长
     */
    public static Long getTimeDelay(Date startTime, Date currTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        Date realStartTime = cal.getTime();
        return realStartTime.getTime() - currTime.getTime();
    }

    /**
     * 获取当前时间的整分钟日期时间
     *
     * @return {@code Date}类型数据
     */
    public static Date getFixedCurrentTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当前时间 n 分钟之后的整分钟日期时间戳
     *
     * @param n 推迟时长，单位为分钟
     * @return 用于记录的{@code String}类型数据
     */
    public static String getFixedTimestampFromNow(int n) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + n);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date curDate = cal.getTime();
        String timestamp = formatter.format(curDate);
        return timestamp;
    }

    /**
     * 从开始和结束时间戳获取类型为{@code Long}的时差
     *
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 类型为{@code Long}的时差
     */
    public static Long calTimeDuration(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date startDate = null;
        try {
            startDate = fmt.parse(startTime);
        } catch (ParseException e) {
            throw new UnsupportedOperationException();
        }
        Date endDate = null;
        try {
            endDate = fmt.parse(endTime);
        } catch (ParseException e) {
            throw new UnsupportedOperationException();
        }
        Long duration = endDate.getTime() - startDate.getTime();
        return duration;
    }

    public String createTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date curDate = new Date(System.currentTimeMillis());
        String timestamp = formatter.format(curDate);
        return timestamp;
    }

}
