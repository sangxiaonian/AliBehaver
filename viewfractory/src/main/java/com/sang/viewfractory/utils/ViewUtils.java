package com.sang.viewfractory.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2017/2/20 10:29
 */
public class ViewUtils {

    /**
     * 获取两位小数
     *
     * @param data
     * @return
     */
    public static float get2Double(double data) {
        BigDecimal b = new BigDecimal(data);
//        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        //   b.setScale(2,   BigDecimal.ROUND_HALF_UP)   表明四舍五入，保留两位小数
        float v = (float) Double.parseDouble(String.format("%.2f", data));

        return v;
    }

    public static String getTime(String flag, Context context){
        SharedPreferences preferences = context.getSharedPreferences(Config.sp_name, 0);
        String time = formatDateTime(preferences.getLong(flag, 0));
        return time;

    }
  public static void setTime(String flag,Context context){
        if (!TextUtils.isEmpty(flag)) {
            SharedPreferences preferences = context.getSharedPreferences(Config.sp_name, 0);
            preferences.edit().putLong(flag, System.currentTimeMillis()).commit();
        }
    }


    public static String formatDateTime(String dateTime) {
        long l = Long.parseLong(dateTime);
        return formatDateTime(l);
    }

    public static String formatDateTime(long dateTime) {
        String text;
        Date date = new Date(dateTime);
        if (isSameDay(dateTime)) {
            Calendar calendar = GregorianCalendar.getInstance();
            if (inOneMinute(dateTime, calendar.getTimeInMillis())) {
                return "刚刚";
            } else if (inOneHour(dateTime, calendar.getTimeInMillis())) {
                return String.format("%d分钟之前", Math.abs(dateTime - calendar.getTimeInMillis()) / 60000);
            } else {
                calendar.setTime(date);
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                if (hourOfDay > 17) {
                    text = "晚上 hh:mm";
                } else if (hourOfDay >= 0 && hourOfDay <= 6) {
                    text = "凌晨 hh:mm";
                } else if (hourOfDay > 11 && hourOfDay <= 17) {
                    text = "下午 hh:mm";
                } else {
                    text = "上午 hh:mm";
                }
            }
        } else if (isYesterday(dateTime)) {
            text = "昨天 HH:mm";
        } else if (isSameYear(dateTime)) {
            text = "M月d日 HH:mm";
        } else {
            text = "很久以前";
        }

        // 注意，如果使用android.text.format.DateFormat这个工具类，在API 17之前它只支持adEhkMmszy
        return new SimpleDateFormat(text, Locale.CHINA).format(date);
    }

    private static boolean inOneMinute(long time1, long time2) {
        return Math.abs(time1 - time2) < 60000;
    }

    private static boolean inOneHour(long time1, long time2) {
        return Math.abs(time1 - time2) < 3600000;
    }

    private static boolean isSameDay(long time) {
        long startTime = floorDay(Calendar.getInstance()).getTimeInMillis();
        long endTime = ceilDay(Calendar.getInstance()).getTimeInMillis();
        return time > startTime && time < endTime;
    }

    private static boolean isYesterday(long time) {
        Calendar startCal;
        startCal = floorDay(Calendar.getInstance());
        startCal.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = startCal.getTimeInMillis();

        Calendar endCal;
        endCal = ceilDay(Calendar.getInstance());
        endCal.add(Calendar.DAY_OF_MONTH, -1);
        long endTime = endCal.getTimeInMillis();
        return time > startTime && time < endTime;
    }

    private static boolean isSameYear(long time) {
        Calendar startCal;
        startCal = floorDay(Calendar.getInstance());
        startCal.set(Calendar.MONTH, Calendar.JANUARY);
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        return time >= startCal.getTimeInMillis();
    }

    private static Calendar floorDay(Calendar startCal) {
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        return startCal;
    }

    private static Calendar ceilDay(Calendar endCal) {
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        return endCal;
    }

    /**
     * 获取View在屏幕中的位置
     * @param view
     * @return
     */
    public static int[] getLoaction(View view){
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position;
    }


    /**
     * 获取View在屏幕中的位置
     * @param view
     * @return
     */
    public static Rect getViewRect(View view){
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return rect;
    }

}
