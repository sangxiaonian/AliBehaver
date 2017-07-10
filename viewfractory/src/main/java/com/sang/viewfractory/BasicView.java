package com.sang.viewfractory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.sang.viewfractory.factory.ShapFactory;
import com.sang.viewfractory.utils.Apputils;
import com.sang.viewfractory.utils.UnOverWriteException;


/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/12/6 10:49
 */
public class BasicView extends View {

    /**
     * 正在加载
     */
    public static final int LOADING = 0;
    /**
     * 加载成功
     */
    public static final int LOAD_SUCCESS = 1;//加载成功

    /**
     * 加载失败
     */
    public static final int LOAD_FAIL = 2;//加载失败
    /**
     * 箭头向下
     */
    public static final int LOAD_OVER = 4;//正常状态
    /**
     * 箭头向上
     */
    public static final int LOAD_BEFOR = 5;//即将开始加载

    /**
     * 加载中旋转图像
     */
    public static final int STYLE_LOAD = 6;

    /**
     * 加载中正方形图像
     */
    public static final int STYLE_SQUARE = 7;
    /**
     * 没有更多数据了
     */
    public static final int LOAD_NO_MORE=8;

    /**
     * 当前状态
     */
    protected int state;

    /**
     * 间隙
     */
    protected int gap;
    protected Paint mPaint;
    protected Path mPath;
    /**
     * 画笔颜色
     */
    protected int color;

    /**
     * 宽高
     */
    protected int mWidth, mHeight;


    /**
     * 图形绘制工厂
     */
    protected ShapFactory factory;


    public BasicView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public BasicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public BasicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        style=STYLE_SQUARE;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
        color = Color.parseColor("#B8B7B8");
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        upState(LOAD_BEFOR);
        gap = Apputils.dip2px(getContext(), 5);
        mPath = new Path();
        factory = ShapFactory.getInstance(mPath, mPaint,context);
        style=STYLE_SQUARE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, heitht;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (getPaddingLeft() + Apputils.dip2px(getContext(), 40) + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            heitht = heightSize;
        } else {
            heitht = (getPaddingTop() + Apputils.dip2px(getContext(), 40) + getPaddingBottom());
        }
        setMeasuredDimension(width, heitht);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWidth == 0) {
            mHeight = mWidth = Math.max(getMeasuredWidth(), getMeasuredHeight());
        }
        Bitmap bitmap = creatBitmap(state);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
            bitmap.recycle();
        }
    }


    /**
     * 根据当前所处状态来绘制不同的图形
     *
     * @param state
     * @eturn
     */
    private Bitmap creatBitmap(int state) {
        Bitmap bitmap = null;
        switch (state) {
            case LOAD_BEFOR:
            case LOAD_OVER:
                bitmap = factory.creatArrows(mWidth, mHeight, 12);
                break;
            case LOADING:
                setShapRotation(0);
                bitmap = creatShape(mWidth, mHeight);
                break;
            case LOAD_SUCCESS:
                setShapRotation(0);
                bitmap = factory.creatCorrect(mWidth, mHeight);
                break;
            case LOAD_FAIL:
                setShapRotation(0);
                bitmap = factory.creatError(mWidth, mHeight);
                break;
            case LOAD_NO_MORE:
                setShapRotation(0);

                break;

        }
        mPath.reset();
        return bitmap;
    }


    /**
     * 绘制加载中的动画,必须重写
     *
     * @param mWidth  高度
     * @param mHeight 宽度
     */
    protected Bitmap creatShape(int mWidth, int mHeight) {
        throw new UnOverWriteException("No OverWrite creatShap()");

    }


    protected ValueAnimator rota, flip;

    private float startAng, endAng;

    /**
     * 箭头变化动画
     *
     * @param state
     */
    private void round(final int state) {

        if (state == this.state) {
            return;
        }

        clearViewAnimation();
        startAng = getRotation();
        switch (state) {
            case LOAD_OVER:
                endAng = 0;
                if (rota != null && rota.isRunning()) {
                    startAng = (float) rota.getAnimatedValue();
                    rota.cancel();
                }
                break;
            case LOAD_BEFOR:
                endAng = 180;
                if (rota != null && rota.isRunning()) {
                    startAng = (float) rota.getAnimatedValue();
                    rota.cancel();
                }
                break;
            default:
                startAng = 0;
                endAng = 0;
                break;
        }

        rota = ValueAnimator.ofFloat(startAng, endAng);
        rota.setDuration(200);
        rota.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                setShapRotation((Float) animation.getAnimatedValue());
            }
        });
        rota.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);

                setShapRotation(0);
            }
        });

        if (startAng != endAng) {
            rota.start();
        }

    }


    private void flip(final int state) {
        if (state == this.state) {
            return;
        }
      clearViewAnimation();
        switch (state) {
            case LOADING:
                flipAnimation();
                break;
            case LOAD_SUCCESS:
                if (flip != null || flip.isRunning()) {
                    flip.cancel();
                }
                break;
        }


    }




    /**
     * 加载状态时候的动画
     */
    protected void flipAnimation() {
        clearViewAnimation();
    }

    public void upState(int state) {
        if (this.state == state) {
            return;
        }
        clearViewAnimation();

        if (state == LOAD_BEFOR || state == LOAD_OVER) {
            round(state);
        } else if (state == LOADING) {
            flip(state);
        }else if (state==LOAD_SUCCESS){
            clearViewAnimation();
        }
        this.state = state;
        postInvalidate();
    }

    protected void clearViewAnimation(){
        if (flip != null && flip.isRunning()) {
            flip.cancel();

        }
        if (rota != null && rota.isRunning()) {
            rota.cancel();
        }
    }


    protected void setShapRotation(float rotation) {
        setPivotX(mWidth / 2);
        setPivotY(mHeight / 2);
        setRotation(rotation);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (state == LOADING) {
            flipAnimation();
        }
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (flip != null && flip.isRunning()) {
            flip.cancel();
        }
        clearViewAnimation();
    }

    protected int style;
    public void setStyle(int style) {
        this.style=style;
    }
}
