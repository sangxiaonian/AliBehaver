package com.sang.viewfractory.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sang.viewfractory.BasicView;
import com.sang.viewfractory.utils.Apputils;
import com.sang.viewfractory.utils.ViewUtils;


/**
 * Description：下拉刷新
 *
 * @Author：桑小年
 * @Data：2016/12/9 10:50
 */
public class RefrushLinearLayout extends LinearLayout {

    public final static int STYLE_LOAD=ShapeView.STYLE_LOAD;
    public final static int STYLE_SQUARE=ShapeView.STYLE_SQUARE;
    private TextView tvMsg, tvTime;
    private BasicView shapeView;

    private String flag;

    public RefrushLinearLayout(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public RefrushLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public RefrushLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    LinearLayout l;
    int gap;
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {

          gap = Apputils.dip2px(context, 5);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        shapeView = new ShapeView(context);
        LayoutParams params = new LayoutParams(Apputils.dip2px(context, 30), Apputils.dip2px(context, 30));
        shapeView.setLayoutParams(params);
        tvMsg = new TextView(context);
        tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, Apputils.sp2px(context, 16));
        tvMsg.setText("准备刷新数据");
        tvTime = new TextView(context);
        tvTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, Apputils.sp2px(context, 12));
        l = new LinearLayout(context);
        l.addView(tvMsg);
        l.addView(tvTime);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(gap, 0, 0, 0);
        addView(shapeView);
        addView(l);
        setPadding(gap, gap, gap, gap);
    }

    public void setStyle(int style){
        shapeView.setStyle(style);
    }




    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setOrientation(HORIZONTAL);
    }

    /**
     * 设置显示数据
     *
     * @param msg
     */
    public void setTvMsg(String msg) {
        tvMsg.setText(msg);
    }


    /**
     * 设置刷新时间
     *
     * @param flag    设置刷新时间的view标志
     * @param context 上下文
     */
    public void setTvTime(String flag, Context context) {
        tvTime.setText("上次刷新:" + ViewUtils.getTime(flag, context));

    }

    /**
     * 设置刷新时间的flag
     *
     * @param flag 用来判断当前刷新的View,如果不设置,则无法显示刷新时间
     */
    public void setFlag(String flag) {
        this.flag = flag;
        tvTime.setVisibility(VISIBLE);
    }


    public void upState(int state) {
        shapeView.upState(state);
        if (state == ShapeView.LOAD_SUCCESS) {
            if (!TextUtils.isEmpty(flag)) {
                ViewUtils.setTime(flag, getContext());
            }
        }else {
            if (!TextUtils.isEmpty(flag)) {
                setTvTime(flag,getContext());
                tvTime.setVisibility(VISIBLE);
            }else {
                tvTime.setVisibility(GONE);

            }
        }
    }

    public String getTest() {
        return tvMsg.getText().toString();
    }

}
