package em.sang.com.allrecycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sang.viewfractory.utils.Apputils;
import com.sang.viewfractory.utils.DeviceUtils;
import com.sang.viewfractory.utils.JLog;
import com.sang.viewfractory.view.RefrushLinearLayout;

import em.sang.com.allrecycleview.adapter.BasicAdapter;
import em.sang.com.allrecycleview.holder.SimpleHolder;


/**
 * Description：上拉刷新和下拉加载控件,默认没有上拉加载
 * <p>
 * Author：桑小年
 * Data：2016/12/1 14:42
 */
public class RefrushRecycleView extends BasicRefrushRecycleView {

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    public RefrushRecycleView(Context context) {
        super(context);

    }

    public RefrushRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public RefrushRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void initView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super.initView(context, attrs, defStyle);
        downY = -1;
        topView = new RefrushLinearLayout(context);
        boomView = new RefrushLinearLayout(context);
        setStyle(style);
        mearchTop = Apputils.getWidthAndHeight(topView)[1];
        mearchBoom = Apputils.getWidthAndHeight(boomView)[1];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = (int) min;
        topView.setLayoutParams(params);

        LinearLayout.LayoutParams boom = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        boom.height = (int) min;
        boomView.setLayoutParams(boom);
        hasTop = true;
        hasBoom = false;


    }


    @Override
    protected int upChangeStateByHeight(int height) {
        int state;
        if (height > mearchTop) {
            state = LOAD_BEFOR;
        } else {
            state = LOAD_OVER;

        }
        return state;
    }

    @Override
    protected boolean isDownChangeStateByHeight() {
        if (style == STYLE_PULL) {
            return super.isDownChangeStateByHeight();
        } else {
            return false;
        }
    }

    private long lastTime;

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (downstate != LOADING_DOWN) {
            synchronized (RefrushRecycleView.class) {
                if (dy > DeviceUtils.getMinTouchSlop(getContext()) && style == STYLE_SLIPE) {
                    if (!isFirst() && isLast() && downstate != LOADING_DOWN) {
                        long l = System.currentTimeMillis() - lastTime;
                        if (l > 1000) {
                            JLog.i("-------------被叫用了---------------------");
                            lastTime = System.currentTimeMillis();
                            downRefrushState(LOADING_DOWN);
                        }
                    }
                }
            }
        }
    }


    @Override
    protected float getDownHeightByState(int upState) {
        if (style == STYLE_PULL) {
            return super.getDownHeightByState(upState);
        } else {
            return mearchBoom;
        }
    }

    @Override
    protected int downChangeStateByHeight(int height) {
        int state;
        if (height > mearchBoom) {
            state = LOAD_DOWN_BEFOR;
        } else {
            state = LOAD_DOWN_OVER;

        }
        return state;
    }

    @Override
    protected int getNextState(int refrush_state) {
        int load = refrush_state;
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
            case LOAD_SUCCESS:
            case LOAD_FAIL:
                load = LOAD_OVER;
                break;
            case LOADING:
            case LOADING_DOWN:
            case LOAD_OVER:
            case LOAD_DOWN_OVER:
                break;

            case LOAD_DOWN_SUCCESS:
            case LOAD_DOWN_FAIL:
                if (style != STYLE_SLIPE) {
                    load = LOAD_DOWN_OVER;
                }
                break;
        }
        return load;
    }

    @Override
    public void addUpDataItem(Adapter adapter) {
        if (adapter instanceof BasicAdapter) {
            BasicAdapter basicAdapter = (BasicAdapter) adapter;
            if (getHasBoom()) {
                basicAdapter.addBoom(new SimpleHolder(boomView));
            }
            if (getHasTop()) {
                basicAdapter.addTop(new SimpleHolder(topView));
            }
        }
    }


    @Override
    public boolean canDrag() {
        return isLast() || isFirst();
    }


}
