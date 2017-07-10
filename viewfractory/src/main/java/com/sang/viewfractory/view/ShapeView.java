package com.sang.viewfractory.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.sang.viewfractory.BasicView;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/12/6 10:49
 */
public class ShapeView extends BasicView {




    public ShapeView(Context context) {
        super(context);


    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }



    private boolean change;
    protected void flipAnimation() {
        clearViewAnimation();

        flip=creatAnimotion();
        flip.setInterpolator(new LinearInterpolator());
        flip.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                switch (style){
                    case STYLE_SQUARE:
                        if (change) {
                            setScaleX(value);
                        } else {
                            setScaleY(value);
                        }
                        break;
                    case STYLE_LOAD:

                        setRotation(360*value);
                        break;
                }


            }
        });
        flip.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                change = !change;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                setScaleX(1);
                setScaleY(1);

            }
        });
        flip.setRepeatCount(Integer.MAX_VALUE);
        long time = getTime();
        flip.setDuration(time);
        flip.start();
    }

    private ValueAnimator creatAnimotion() {
        ValueAnimator animator ;
        switch (style){
            case STYLE_LOAD:
                animator = ValueAnimator.ofFloat(1f,0f);
                break;
            default:
                animator = ValueAnimator.ofFloat(1f, 0f, 1f);
                break;
        }

        return animator;
    }

    /**
     * 动画执行时间
     * @return
     */
    private long getTime() {
        long time ;
        switch (style){
            case STYLE_LOAD:
                time=5000;
                break;
            default:
                time=1000;
                break;
        }
        return time;
    }

    @Override
    protected Bitmap creatShape(int mWidth,int mHeight) {
        Bitmap bitmap;
        switch (style){
            case STYLE_LOAD:
                  bitmap = factory.creatLoading(mWidth, mHeight);
                break;
            default:
                bitmap= factory.creatShap(mWidth,mHeight);
                break;
        }
        return bitmap;
    }
}
