package em.sang.com.allrecycleview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.sang.viewfractory.view.RefrushLinearLayout;
import com.sang.viewfractory.view.ShapeView;

import em.sang.com.allrecycleview.inter.RefrushListener;


/**
 *
 * 是时候撸点真正的代码了！！！
 *
 * 创建人：桑小年
 * 日期：  2017/6/2
 *
 * 功能描述：基础刷新类
 */
public abstract class BasicRefrushRecycleView extends BaiscRecycleView {
    /**
     * 正在加载
     */
    public static final int LOADING = 0;//正在加载
    /**
     * 加载成功
     */
    public static final int LOAD_SUCCESS = 1;//加载成功

    /**
     * 加载失败
     */
    public static final int LOAD_FAIL = 2;//加载失败
    /**
     * 下拉加载
     */
    public static final int LOAD_OVER = 4;//下拉加载
    /**
     * 松手刷新
     */
    public static final int LOAD_BEFOR = 5;//松手刷新


    /**
     * 正在加载
     */
    public static final int LOADING_DOWN = 6;//正在加载
    /**
     * 加载成功
     */
    public static final int LOAD_DOWN_SUCCESS = 7;//加载成功

    /**
     * 加载失败
     */
    public static final int LOAD_DOWN_FAIL = 8;//加载失败
    /**
     * 上拉加载
     */
    public static final int LOAD_DOWN_OVER = 9;//下拉加载
    /**
     * 松手刷新
     */
    public static final int LOAD_DOWN_BEFOR = 10;//松手刷新
    public static final int LOAD_DOWN_NO_MORE = 11;//松手刷新


    /**
     * 不需要上拉,只需要滑动到最底部,就能直接调用加载更多
     */
    public static final int STYLE_SLIPE = 11;
    /**
     * 滑动到最底部后,需要拖动才能直接调用加载更多
     */
    public static final int STYLE_PULL = 12;


    public boolean hasTop, hasBoom;

    public RefrushLinearLayout topView, boomView;
    public int mearchTop, mearchBoom;

    /**
     * 刷新控件的style
     */
    protected int style = STYLE_SLIPE;

    public float downY;


    public boolean isNoTouch = true;
    protected boolean isLoadMore;
    protected ValueAnimator upAnimator, downAnimator;


    /**
     * 滑动距离倍数
     */
    public final int muli = 3;
    public RefrushListener listener;
    public float min = 1;
    public int upstate = -1;
    public int downstate = -1;


    public BasicRefrushRecycleView(Context context) {
        super(context);


    }

    public BasicRefrushRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public BasicRefrushRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }


    protected int getNextState(int refrush_state) {
        int load = 0;
        switch (refrush_state) {
            case LOAD_DOWN_BEFOR:
                if (isNoTouch) {
                    load = LOADING_DOWN;
                }
                break;
            case LOAD_BEFOR:
                if (isNoTouch) {
                    load = LOADING;
                }
                break;
            case LOADING:
            case LOADING_DOWN:
            case LOAD_OVER:
            case LOAD_DOWN_OVER:
                break;
            case LOAD_SUCCESS:
            case LOAD_FAIL:
                load = LOAD_OVER;
                break;
            case LOAD_DOWN_SUCCESS:
            case LOAD_DOWN_FAIL:
            case LOAD_DOWN_NO_MORE:
                load = LOAD_DOWN_OVER;
                break;
            default:
                load=refrush_state;
                break;
        }
        return load;
    }


    /**
     * 是否是上拉加载更多
     *
     * @return
     */
    public boolean isLoadMore() {
        return isLoadMore;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (downY == -1) {
            downY = e.getRawY();
        }

        if (listener!=null) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = e.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float gap = (e.getRawY() - downY) / muli;
                    downY = e.getRawY();
                    isNoTouch = false;
                    if (isFirst()) {
                        clearUpAnimotion();
                        setUpHeightVisible(gap);
                    } else if (isLast()) {
                        clearDownAnimotion();
                        setDownHeightVisible(-gap);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    downY = -1;
                    isNoTouch = true;
                    if (isFirst()) {
                        startUpAnimotion(upstate);
                    } else if (isLast()) {
                        startDownAnimotion(downstate);
                    }
                    break;
                default:
                    downY = -1;
                    isNoTouch = true;
                    if (isFirst()) {
                        startUpAnimotion(upstate);
                    } else if (isLast()) {
                        startDownAnimotion(downstate);
                    }
                    break;
            }
        }
        return super.onTouchEvent(e);
    }

    /**
     * 设置下拉刷新布局高度改变
     *
     * @param gap 增加的高度
     */
    protected void setUpHeightVisible(float gap) {
        LinearLayout view = topView;
        int height = (int) (getHeightVisiable(view) + gap);
        float minheight;

        if (isUpChangeStateByHeight()) {
            minheight = min;
        } else {
            minheight = mearchTop;
        }
        if (height > minheight) {
            scrollToPosition(0);

        }
        height = height >= minheight ? height : (int) minheight;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
        //根据高度更新状态
        if (isUpChangeStateByHeight()) {
            upRefushState(upChangeStateByHeight(height));
        }
    }

    /**
     * 下拉刷新时候,根据条目高度来改变状态,主要用于下拉刷新和松手刷新时候调用
     *
     * @param height 当前条目高度
     * @return 返回更改的状态
     */
    protected abstract int upChangeStateByHeight(int height);

    /**
     * 下拉刷新动画
     *
     * @param upState 当前状态
     */
    protected void startUpAnimotion(int upState) {
        final View view = topView;
        final float stand = getUpHeightByState(upState);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int height = layoutParams.height;

        if (height == stand) {
            return;
        }

        final int load = getNextState(upState);

        upAnimator = ValueAnimator.ofFloat(height, stand);
        upAnimator.setInterpolator(new DecelerateInterpolator(1));
        upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setViewHeight(topView, value);
            }
        });
        upAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (load != upstate) {
                    upRefushState(load);
                }

            }
        });

        if (isNoTouch) {
            if (upState == LOAD_SUCCESS || upState == LOAD_FAIL) {
                upAnimator.setDuration(1000);
                upAnimator.start();
            } else {
                upAnimator.setDuration(200);
                upAnimator.start();
            }
        }

    }

    protected float getUpHeightByState(int upState) {
        float stand;
        switch (upState) {
            case LOAD_BEFOR:
            case LOADING:
                stand = mearchTop;
                break;
            default:
                stand = min;
                break;
        }
        return stand;
    }

    /**
     * 上拉加载动画
     *
     * @param downState 当前状态
     */
    protected void startDownAnimotion(final int downState) {
        final View view = boomView;
        float stand = getDownHeightByState(downState);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int height = layoutParams.height;
        if (height == stand) {
            return;
        }

        final int load = getNextState(downState);


        downAnimator = ValueAnimator.ofFloat(height, stand);
        downAnimator.setInterpolator(new DecelerateInterpolator(1));
        downAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setViewHeight(boomView, value);
            }
        });
        downAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (load != downstate&&style!=STYLE_SLIPE) {
                    downRefrushState(load);
                }

            }
        });

        if (isNoTouch) {
            if (downstate == LOAD_SUCCESS || downstate == LOAD_FAIL) {
                downAnimator.setDuration(1000);
                downAnimator.start();
            } else {
                downAnimator.setDuration(200);
                downAnimator.start();
            }
        }
    }

    protected float getDownHeightByState(int upState) {
        float stand;
        switch (upState) {
            case LOADING_DOWN:
            case LOAD_DOWN_BEFOR:
                stand = mearchBoom;
                break;
            default:
                stand = min;
                break;
        }
        return stand;
    }

    /**
     * 设置上拉加载布局高度改变
     *
     * @param gap 增加的高度
     */
    protected void setDownHeightVisible(float gap) {
        LinearLayout view = boomView;
        int height = (int) (getHeightVisiable(view) + gap);
        float minheight;

        if (isDownChangeStateByHeight()) {
            minheight = min;
        } else {
            minheight = mearchBoom;
        }

        if (height > minheight) {
            scrollToPosition(getAdapter().getItemCount() - 1);
        }
        height = height >= minheight ? height : (int) minheight;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
        //根据高度更新状态
        if (isDownChangeStateByHeight()) {
            downRefrushState(downChangeStateByHeight(height));
        }
    }

    /**
     * 上拉加载时候,用来根据高度判断是处于上拉加载还是松手刷新
     *
     * @param height 条目高度
     * @return 状态
     */
    protected abstract int downChangeStateByHeight(int height);


    private boolean isStrong;

    /**
     * 设置正在加载
     */
    public void setLoading() {
        if (listener!=null) {
            clearViewAnimotion();
            isStrong = true;
            setViewHeight(topView, mearchTop);
            upRefushState(LOADING);
        }
    }


    /**
     * 获取当前控件高度
     *
     * @return
     * @pram view
     */
    public int getHeightVisiable(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        return layoutParams.height;
    }


    public void clearViewAnimotion() {
        clearUpAnimotion();
        clearDownAnimotion();
    }

    public void clearUpAnimotion() {
        if (upAnimator != null && upAnimator.isRunning()) {
            upAnimator.removeAllUpdateListeners();
            upAnimator.pause();
            upAnimator.cancel();
        }
    }

    public void clearDownAnimotion() {
        if (downAnimator != null && downAnimator.isRunning()) {
            downAnimator.removeAllUpdateListeners();
            downAnimator.pause();
            downAnimator.cancel();

        }
    }


    /**
     * 更改View的高度
     *
     * @param view  要更改的条目
     * @param value 高度值
     */
    public void setViewHeight(View view, float value) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) value;
        view.setLayoutParams(layoutParams);
    }

    @Override
    public boolean isFirst() {
        return super.isFirst() && hasTop;
    }

    @Override
    public boolean isLast() {
        return super.isLast() && hasBoom;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        addUpDataItem(adapter);
        super.setAdapter(adapter);
    }


    public void loadNoMore() {
        loadNoMore(null);
    }

    public void loadNoMore(String msg) {

        if (isFirst()) {
            if (TextUtils.isEmpty(msg)) {
                msg = getContext().getString(R.string.no_data);
            }
            upRefushState(LOAD_FAIL, msg);
        } else {
            if (TextUtils.isEmpty(msg)) {
                msg = getContext().getString(R.string.no_more);
            }
            downRefrushState(LOAD_DOWN_NO_MORE, msg);
        }
    }


    /**
     * 上拉加载更多
     *
     * @param refrush_state
     * @param s
     */
    protected void downRefrushState(int refrush_state, String s) {
        if (this.downstate != refrush_state) {
            synchronized (BasicRefrushRecycleView.class) {
                if (this.downstate != refrush_state) {
                    int loadstate = -1;
                    String msg = null;
                    boomView.setVisibility(VISIBLE);
                    switch (refrush_state) {
                        case LOAD_DOWN_OVER:
                            loadstate = ShapeView.LOAD_BEFOR;
                            msg = "上拉加载数据";
                            break;
                        case LOAD_DOWN_BEFOR:
                            loadstate = ShapeView.LOAD_OVER;
                            msg = "松手刷新数据";

                            break;
                        case LOADING_DOWN:
                            loadstate = ShapeView.LOADING;
                            msg = "正在加载数据";
                            if (listener != null) {
                                isLoadMore = true;
                                listener.onLoadDowning();
                            }
                            if (upstate!=LOAD_OVER) {
                                upRefushState(LOAD_OVER);
                            }
                            break;
                        case LOAD_DOWN_FAIL:
                            loadstate = ShapeView.LOAD_FAIL;
                            msg = "加载失败";
                            break;
                        case LOAD_DOWN_SUCCESS:
                            loadstate = ShapeView.LOAD_SUCCESS;
                            msg = "加载成功";
                            break;
                        case LOAD_DOWN_NO_MORE:
                            loadstate=ShapeView.LOAD_NO_MORE;
                            msg = "没有更多数据了";
                            break;
                        default:
                            loadstate = ShapeView.LOAD_FAIL;
                            msg = "加载异常";
                            break;
                    }

                    boomView.upState(loadstate);
                    if (TextUtils.isEmpty(s)) {
                        boomView.setTvMsg(msg);
                    } else {
                        boomView.setTvMsg(s);
                    }
                    this.downstate = refrush_state;
                    startDownAnimotion(downstate);
                }
            }
        }
    }

    /**
     * 上拉加载更多
     *
     * @param refrush_state
     */
    protected void downRefrushState(int refrush_state) {
        downRefrushState(refrush_state, null);
    }

    public void upRefushState(int refrush_state, String s) {
        if (this.upstate != refrush_state) {
            synchronized (BasicRefrushRecycleView.class) {
                if (this.upstate != refrush_state) {
                    int loadstate = -1;
                    String msg = null;
                    switch (refrush_state) {
                        case LOAD_OVER://4
                            loadstate = ShapeView.LOAD_OVER;
                            msg = "下拉刷新数据";
                            break;
                        case LOAD_BEFOR://5
                            loadstate = ShapeView.LOAD_BEFOR;
                            msg = "松手刷新数据";
                            break;
                        case LOADING://0
                            loadstate = ShapeView.LOADING;
                            msg = "正在加载数据";
                            if (listener != null) {
                                isLoadMore = false;
                                listener.onLoading();
                            }

                            downRefrushState(LOAD_DOWN_OVER);
                            boomView.setVisibility(INVISIBLE);
                            break;
                        case LOAD_FAIL://2
                            loadstate = ShapeView.LOAD_FAIL;
                            msg = "加载失败";
                            break;
                        case LOAD_SUCCESS://1
                            loadstate = ShapeView.LOAD_SUCCESS;
                            msg = "加载成功";

                            break;
                        default:
                            loadstate = ShapeView.LOAD_FAIL;
                            msg = "加载异常";
                            break;
                    }
                    topView.upState(loadstate);
                    if (TextUtils.isEmpty(s)) {
                        topView.setTvMsg(msg);
                    } else {
                        topView.setTvMsg(s);
                    }
                    this.upstate = refrush_state;
                    startUpAnimotion(upstate);
                }
            }
        }
    }

    /**
     * 下拉状态刷新
     *
     * @param refrush_state
     */
    protected void upRefushState(int refrush_state) {
        upRefushState(refrush_state, null);

    }


    public void setFlag(String flag) {
        if (topView != null) {
            topView.setFlag(flag);
        }


    }

    public void setDownFlag(String flag) {
        if (boomView != null) {
            boomView.setFlag(flag);
        }
    }


    /**
     * 将刷新条目添加到adapter上
     *
     * @param adapter
     */
    public abstract void addUpDataItem(Adapter adapter);

    /**
     * 向上拖动，改变数据
     *
     * @return
     */
    public abstract boolean canDrag();

    public View getTopView() {
        return topView;
    }

    public View getBoomView() {
        return boomView;
    }

    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }

    public void setHasBoom(boolean hasBoom) {
        this.hasBoom = hasBoom;
    }

    public boolean getHasTop() {
        return hasTop;
    }

    public boolean getHasBoom() {
        return hasBoom;
    }


    /**
     * 下拉刷新时候,是否能够随着高度改变状态
     *
     * @return true 可以
     */
    protected boolean isUpChangeStateByHeight() {
        return upstate == LOAD_OVER || upstate == LOAD_BEFOR || upstate == -1;
    }

    /**
     * 上拉加载时候,是否能够随着高度改变状态
     *
     * @return true 可以
     */
    protected boolean isDownChangeStateByHeight() {
        return downstate == LOAD_DOWN_OVER || downstate == LOAD_DOWN_BEFOR || downstate == -1;
    }

    /**
     * 设置刷新监听
     *
     * @param listener
     */
    public void setRefrushListener(RefrushListener listener) {
        this.listener = listener;
    }

    /**
     * 刷新成功后调用
     */
    public void loadSuccess() {
        loadSuccess(null);
    }


    /**
     * 刷新成功后调用
     */
    public void loadSuccess(String msg) {

        if (!isLoadMore()) {
            upRefushState(LOAD_SUCCESS);
        } else {
            downRefrushState(LOAD_DOWN_SUCCESS);
        }
    }

    /**
     * 刷新失败后调用
     */
    public void loadFail() {
        loadFail(null);

    }

    /**
     * 刷新失败后调用
     */
    public void loadFail(String msg) {
        if (!isLoadMore()) {
            upRefushState(LOAD_FAIL, msg);
        } else {
            downRefrushState(LOAD_DOWN_FAIL, msg);
        }

    }

    public View getEndView() {
        if (isFirst()) {
            return topView;
        } else if (isLast()) {
            return boomView;
        }
        return null;
    }


    /**
     * 设置加载风格
     *
     * @param style
     */
    public void setStyle(int style) {
        this.style = style;
        int top, down;
        switch (style) {
            case STYLE_PULL:
                top = RefrushLinearLayout.STYLE_SQUARE;
                down = RefrushLinearLayout.STYLE_LOAD;
                break;
            case STYLE_SLIPE:
                top = RefrushLinearLayout.STYLE_SQUARE;
                down = RefrushLinearLayout.STYLE_LOAD;
                break;
            default:
                top = RefrushLinearLayout.STYLE_SQUARE;
                down = RefrushLinearLayout.STYLE_LOAD;
                break;
        }

        topView.setStyle(top);
        boomView.setStyle(down);
    }


}
