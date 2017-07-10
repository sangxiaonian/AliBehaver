package com.sang.viewfractory.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.sang.viewfractory.utils.DeviceUtils;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/12/8 17:38
 */
public class ShapFactory {

    private Path mPath;
    private Paint mPaint;
    private static ShapFactory factory;
    private Context context;

    public static ShapFactory getInstance(Path mPath, Paint mPaint) {
        factory = new ShapFactory(mPath, mPaint);
        return factory;
    }
    public static ShapFactory getInstance(Path mPath, Paint mPaint,Context context) {
        factory = new ShapFactory(mPath, mPaint,context);
        return factory;
    }

    private ShapFactory(Path mPath, Paint mPaint) {
        this.mPaint = mPaint;
        this.mPath = mPath;
    }
    private ShapFactory(Path mPath, Paint mPaint,Context context) {
        this.mPaint = mPaint;
        this.mPath = mPath;
        this.context = context;

    }

    /**
     * 绘制一个箭头
     *
     * @param mWidth  控件高度
     * @param mHeight 控件宽
     * @param den     箭头杆空格数量
     * @return 一个向下的箭头
     */
    public Bitmap creatArrows(int mWidth, int mHeight, int den) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mPath.reset();
        mPaint.setStyle(Paint.Style.FILL);
        int amount = 0;
        for (int i = 1; i <= den; i++) {
            amount += i;
        }
        float start = mHeight * 2 / 3;
        float l = (float) ((mHeight - start) / (Math.sin(60 * Math.PI / 180) * 2));
        mPath.moveTo(mWidth / 2, mHeight);
        mPath.lineTo(mWidth / 2 - l, start);
        mPath.lineTo(mWidth / 2 + l, start);
        mPath.lineTo(mWidth / 2, mHeight);
        int count = 0;
        int last = 0;
        float left = mWidth / 2 - l / 2;
        float right = mWidth / 2 + l / 2;
        for (int i = den; i > 0; i--) {
            last = count;
            count += i;
            if (i % 2 == 0) {
                float top = start - start * last / (amount);
                float boom = start - start * count / (amount);
                mPath.addRect(left, top, right, boom, Path.Direction.CCW);
            }
        }
        canvas.drawPath(mPath, mPaint);
        return bitmap;
    }

    /**
     * 绘制一个方块
     *
     * @param mWidth  宽
     * @param mHeight 高
     * @return 一个四方形的Bitmap
     */
    public Bitmap creatShap(int mWidth, int mHeight) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mPath.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPath.addRect(0, 0, mWidth , mHeight, Path.Direction.CCW);
        canvas.drawPath(mPath, mPaint);
        return bitmap;
    }

    /**
     * 绘制对号
     * @param mWidth
     * @param mHeight
     * @return
     */
    public Bitmap creatCorrect(int mWidth, int mHeight) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mPath.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        int radius = mWidth /2-2;
        mPath.moveTo(mWidth/8,mHeight/3);
        mPath.lineTo(mWidth/4,mHeight/4);
        mPath.lineTo(mWidth*3/8,mHeight/3);
        mPath.moveTo(mWidth*5/8,mHeight/3);
        mPath.lineTo(mWidth*3/4,mHeight/4);
        mPath.lineTo(mWidth*7/8,mHeight/3);

        mPath.moveTo(mWidth/4,mHeight*3/5);
        mPath.cubicTo(mWidth/4,mHeight*3/5,mWidth/2,mHeight,mWidth*3/4,mHeight*3/5);
        int centerX = mWidth / 2;
        int centerY = mHeight / 2;
        mPath.addCircle(centerX,centerY,radius, Path.Direction.CCW);
        canvas.drawPath(mPath, mPaint);
        return bitmap;
    }

    /**
     * 绘制错号
     * @param mWidth
     * @param mHeight
     * @return
     */
    public Bitmap creatError(int mWidth, int mHeight) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mPath.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPath.moveTo(mWidth/3,mHeight/3);
        mPath.lineTo(mWidth*2/3,mHeight*2/3);
        mPath.moveTo(mWidth*2/3,mHeight/3);
        mPath.lineTo(mWidth/3,mHeight*2/3);
        canvas.drawPath(mPath, mPaint);
        return bitmap;
    }

    /**
     * 绘制加载
     * @param mWidth
     * @param mHeight
     * @return
     */
    public Bitmap creatLoading(int mWidth, int mHeight) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(   Color.GRAY);
        mPaint.setStrokeWidth( DeviceUtils.dip2px(context,4));
        int radius = mWidth / 4;
        int centerX = mWidth / 2;
        int centerY = mHeight / 2;
        RectF rectF = new RectF();
        rectF.left=centerX-radius;
        rectF.top=centerY-radius;
        rectF.right=centerX+radius;
        rectF.bottom=centerY+radius;
        for (int i = 0; i < 120; i++) {
            if (i%10==0){
                canvas.drawArc(rectF, 3*i,3,false,mPaint);
            }
        }
        return bitmap;
    }


}
