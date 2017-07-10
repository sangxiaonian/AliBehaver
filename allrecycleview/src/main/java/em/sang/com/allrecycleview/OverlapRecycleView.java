package em.sang.com.allrecycleview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sang.viewfractory.utils.Apputils;
import com.sang.viewfractory.utils.JLog;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.adapter.BasicAdapter;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/12/22 16:38
 */
public class OverlapRecycleView extends RecyclerView {

    private float downX, downY;
    private float childLeft, childTop;
    private List<View> animations;
    private ViewGroup mDecorView;
    private int[] mDecorViewLocation;


    public OverlapRecycleView(Context context) {
        super(context);
        initView();
    }

    public OverlapRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public OverlapRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
//        setLayoutManager(new OverlapManager(getContext()));

        downY = downX = -1;
        mDecorViewLocation = new int[2];
        mDecorView = (FrameLayout) ((Activity) getContext()).getWindow().getDecorView();


        mDecorView.getLocationOnScreen(mDecorViewLocation);
        animations = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (getChildCount() == 0) {
            return super.onTouchEvent(e);
        }

        if (downX == -1 || downY == -1) {
            downX = e.getX();
            downY = e.getY();
        }


        ViewGroup topView = (ViewGroup) getChildAt(getChildCount() - 1);
        TextView at = (TextView) topView.getChildAt(0);
        JLog.i(getChildCount() + "------" + at.getText());


        float x = topView.getX();
        float y = topView.getY();
        if (childTop == 0 || childLeft == 0) {
            childTop = topView.getTop();
            childLeft = topView.getLeft();
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = e.getX();
                downY = e.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = e.getX();
                float currentY = e.getY();
                topView.setX(x + currentX - downX);
                topView.setY(y + currentY - downY);
                nextChildScale(getChildCount() - 2, getCurrentScaleByTopView(topView));
                downX = currentX;
                downY = currentY;
                break;

            case MotionEvent.ACTION_UP:

            default:
                upAction(topView);
                downY = downX = -1;
                break;
        }


        return super.onTouchEvent(e);
    }

    private float caculateExitY(float x1, float y1, float x2, float y2, float x3) {
        return (y2 - y1) * (x3 - x1) / (x2 - x1) + y1;
    }

    private void upAction(final View topView) {
        float targtX = childLeft;
        float targtY = childTop;
        final boolean isChange;
        TimeInterpolator interpolator = new LinearInterpolator();
        View view = topView;
        if (getCurrentScaleByTopView(topView) < 1) {
            interpolator = new OvershootInterpolator();
            isChange = true;
        } else {
            //获取一个镜像
            view = getMirror(topView);
            isChange = false;
            if (topView.getX() > childLeft) {
                targtX = Apputils.getScreenWidth(getContext()) * 2;
            } else {
                targtX = -topView.getWidth() - Apputils.getScreenWidth(getContext());
            }


            float offsetX = getX() - mDecorView.getX();
            float offsetY = getY() - mDecorView.getY();
            targtY = caculateExitY(childLeft + offsetX, childTop + offsetY, view.getX(), view.getY(), targtX);
        }


        final View finalView = view;
        finalView.animate().setDuration(500).x(targtX).y(targtY).setInterpolator(interpolator).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                if (isChange) {
                    nextChildScale(getChildCount() - 2, getCurrentScaleByTopView(topView));
                }
            }
        }).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isChange) {
                    mDecorView.removeView(finalView);
                }
            }
        });
    }


    private ImageView getMirror(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        ImageView img = new ImageView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        img.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] imgs = new int[2];
        view.getLocationOnScreen(imgs);
        img.setX(imgs[0] - mDecorViewLocation[0]);
        img.setY(imgs[1] - mDecorViewLocation[1]);
        view.setVisibility(GONE);
        delTopView();
        mDecorView.addView(img, params);
        return img;
    }

    private void delTopView() {


        if (getAdapter().getItemCount() < 1) {
            return;
        }
        List bodyLists = ((BasicAdapter) getAdapter()).getBodyLists();
        Object o = bodyLists.get(bodyLists.size()- 1);
        bodyLists.remove(o);
//        bodyLists.add(0,o);
        getAdapter().notifyDataSetChanged();
    }

    private float getCurrentScaleByTopView(View topView) {
        float h = Math.abs((topView.getX() - childLeft) / topView.getWidth());
        float v = Math.abs((topView.getY() - childTop) / topView.getHeight());
        return (float) (0.9 + Math.max(h, v) / 4);
    }

    private void nextChildScale(int index, float scale) {
        if (scale >= 1) {
            scale = 1;
        }
        if (index < 0) {
            return;
        }
        View child = getChildAt(index);

        child.setScaleX(scale);
        child.setScaleY(scale);
        int gap = Apputils.dip2px(getContext(), 5);
        float off = child.getHeight() * (1 - scale) / 2;
        child.setTranslationY(gap + off);

        if (index < 1) {
            return;
        }
        View thirldView = getChildAt(index - 1);
        thirldView.setScaleY(scale - 0.1f);
        thirldView.setScaleX(scale - 0.1f);
        off = thirldView.getHeight() * (1 - scale + 0.1f) / 2;
        thirldView.setTranslationY(off + gap * 2);
    }


    public float getChildLeft() {
        return childLeft;
    }

    public float getChildTop() {
        return childTop;
    }


}
