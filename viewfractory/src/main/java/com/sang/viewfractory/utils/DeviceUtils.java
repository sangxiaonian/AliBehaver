package com.sang.viewfractory.utils;

import android.content.Context;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2017/1/4 10:44
 */
public class DeviceUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取判断滑动的最小滑动距离
     * @param context
     * @return
     */
    public static int getMinTouchSlop(Context context){
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 获取屏幕密度
     * @param context
     * @return
     */
    public static float getInch(Context context){
        float density = context.getResources().getDisplayMetrics().density;
        return density;
    }
    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static float getScreenWidth(Context context){
        float density = context.getResources().getDisplayMetrics().widthPixels;
        return density;
    }
    /**
     * 获取屏幕密度
     * @param context
     * @return
     */
    public static float getScreenHeight(Context context){
        float density = context.getResources().getDisplayMetrics().heightPixels;
        return density;
    }
    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatuBarHeight(Context context)   {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }



}
