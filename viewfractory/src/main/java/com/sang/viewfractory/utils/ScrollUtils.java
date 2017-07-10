package com.sang.viewfractory.utils;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;

/**
 * Description：关于滑动的工具类
 *
 * @Author：桑小年
 * @Data：2017/2/8 11:26
 */
public class ScrollUtils {

    private final Context context;
    VelocityTracker velocityTracker;
    private float INFLEXION=0.35f;
    private float mPhysicalCoeff;
    private float mFlingFriction = ViewConfiguration.getScrollFriction();
    private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
    private float ppi;
    private float velocity;

    public ScrollUtils(Context context, VelocityTracker velocityTracker){
        this.velocityTracker=velocityTracker;
        this.context = context;
        initView(context);
    }

    private void initView(Context context){
        ppi =   DeviceUtils.getInch(context);
        mPhysicalCoeff=computeDeceleration(0.85f);
        velocityTracker.computeCurrentVelocity(1000);
        float velocityX = velocityTracker.getXVelocity();
        float velocityY = velocityTracker.getYVelocity();
        velocity = (float) Math.hypot(velocityX, velocityY);
    }



    /**
     * 获取滑动减速
     * @return
     */
    private double getSplineDeceleration(int velocity) {
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    /**
     * 滑动时间
     * @return
     */
    private int getSplineFlingDuration(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return (int) (100.0 * Math.exp(l / decelMinusOne));
    }

    /**
     * 获取惯性滑动的距离获取滑动速度
     * @param distance
     * @return
     */
    public double getVelocitByDistance(float distance){
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        double l = Math.log(Math.abs(distance) / (mFlingFriction * mPhysicalCoeff)) / DECELERATION_RATE * decelMinusOne;
        return Math.exp(l)/INFLEXION* (mFlingFriction * mPhysicalCoeff) ;
    }


    public double getTiemByDistance(float distance){
        double speed = getVelocitByDistance(distance);
        float time = getSplineFlingDuration((int) Math.abs(speed));
        return time;
    }


    /**
     * 根据获取最大滑动距离
     * @return
     */
    public double getSplineFlingDistance(float maxDistance) {
        float velocityX = velocityTracker.getXVelocity();
        float velocityY = velocityTracker.getYVelocity();
        velocity = (float) Math.hypot(velocityX, velocityY);
        final double l = getSplineDeceleration((int) velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        float coeffY = velocity == 0 ? 1.0f : velocityTracker.getYVelocity() / velocity;
        double totalDistance = mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
        int distance = (int) Math.round(totalDistance * coeffY);
        float finalDistance = Math.abs(maxDistance)>=Math.abs(distance)?distance:maxDistance;
        return finalDistance;
    }

    private float computeDeceleration(float friction) {
        return SensorManager.GRAVITY_EARTH   // g (m/s^2)
                * 39.37f               // inch/meter
                *ppi                // pixels per inch
                * friction;
    }

    public static class ViscousFluidInterpolator implements Interpolator {
        /** Controls the viscous fluid effect (how much of it). */
        private static final float VISCOUS_FLUID_SCALE = 8.0f;

        private static final float VISCOUS_FLUID_NORMALIZE;
        private static final float VISCOUS_FLUID_OFFSET;

        static {

            // must be set to 1.0 (used in viscousFluid())
            VISCOUS_FLUID_NORMALIZE = 1.0f / viscousFluid(1.0f);
            // account for very small floating-point error
            VISCOUS_FLUID_OFFSET = 1.0f - VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0f);
        }

        private static float viscousFluid(float x) {
            x *= VISCOUS_FLUID_SCALE;
            if (x < 1.0f) {
                x -= (1.0f - (float)Math.exp(-x));
            } else {
                float start = 0.36787944117f;   // 1/e == exp(-1)
                x = 1.0f - (float)Math.exp(1.0f - x);
                x = start + x * (1.0f - start);
            }
            return x;
        }

        @Override
        public float getInterpolation(float input) {
            final float interpolated = VISCOUS_FLUID_NORMALIZE * viscousFluid(input);
            if (interpolated > 0) {
                return interpolated + VISCOUS_FLUID_OFFSET;
            }
            return interpolated;
        }
    }
}
