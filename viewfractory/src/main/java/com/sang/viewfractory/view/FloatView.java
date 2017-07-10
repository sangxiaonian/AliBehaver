package com.sang.viewfractory.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sang.viewfractory.R;
import com.sang.viewfractory.utils.DeviceUtils;


/**
 * 折叠view,模仿微信朋友圈,可折叠消息
 */
public class FloatView extends LinearLayout {

    private TextView tv;
    private TextView ftv;
    private String showContent;
    private String hiden;
    private int color;

    private int lines;
    private OnStateChangeListener listener;
    private int textColor;
    private float textSize;
    private int flowTextColor;
    private float flowTextSize;
    private int flowBackground;


    public FloatView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private boolean isExpand;

    public interface OnStateChangeListener{
        void onStateChange(boolean isExpand);
    }

    public void setOnStateChangeListener(OnStateChangeListener listener){
        this.listener=listener;
    }


    private void initView(Context context, AttributeSet attrs, int defStyleAttr){

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FloatView);
        textColor = typedArray.getColor(R.styleable.FloatView_textColor, Color.BLACK);
        textSize = typedArray.getDimension(R.styleable.FloatView_textSize, 14);
        lines = typedArray.getInteger(R.styleable.FloatView_showLines, 5);
        flowTextColor = typedArray.getColor(R.styleable.FloatView_flowTextColor, Color.parseColor("#4c69c5"));
        flowTextSize = typedArray.getDimension(R.styleable.FloatView_flowTextSize, textSize);
        flowBackground = typedArray.getResourceId(R.styleable.FloatView_flowBackground, R.drawable.select_tv_bg);
        showContent=typedArray.getString(R.styleable.FloatView_textShow);
        if (TextUtils.isEmpty(showContent)){
            showContent=context.getString(R.string.showContent);
        }
        hiden=typedArray.getString(R.styleable.FloatView_textHide);
        if (TextUtils.isEmpty(hiden)){
            hiden=context.getString(R.string.hiden);
        }

        setOrientation(VERTICAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv = new TextView(context);
        tv.setTextColor(textColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);

        tv.setTextIsSelectable(true);
        tv.setLayoutParams(params);

        ftv =new TextView(context);
        ftv.setTextColor(flowTextColor);
        ftv.setTextSize(TypedValue.COMPLEX_UNIT_PX,flowTextSize);
        ftv.setClickable(true);
        ftv.setText(showContent);
        ftv.setBackground(getResources().getDrawable(flowBackground));
        ftv.setTextColor(flowTextColor);
        LayoutParams imgParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParams.setMargins(0, DeviceUtils.dip2px(context,10),0,0);
        ftv.setLayoutParams(imgParams);
        addView(tv);
        addView(ftv);
        ftv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpand){
                    int lineCount= tv.getLineCount();
                    int height = tv.getLineHeight();
                    changeHeight(Math.max(mheight,lineCount*height));
                    ftv.setText(hiden);
                }else {
                    ftv.setText(showContent);
                    int height = tv.getLineHeight();
                    changeHeight(height*lines);
                }
                if (listener!=null){
                    listener.onStateChange(isExpand);
                }
                isExpand=!isExpand;
            }
        });


    }


    /**
     * 设置显示内容
     * @param text
     */
    public void setText(String text) {
        tv.setText(text);
        changeTv();
    }

    private float mheight;

    private void changeTv() {
        tv.post(new Runnable() {
            @Override
            public void run() {
                int lineCount= tv.getLineCount();
                int height = tv.getLineHeight();
                FloatView.this.mheight=tv.getHeight();
                if (lineCount>lines){
                    isExpand=false;
                    changeHeight(height*lines);
                    ftv.setVisibility(VISIBLE);
                }else {
                    isExpand=true;
                    ftv.setVisibility(GONE);
                }

            }
        });
    }


    private void changeHeight(float h){
        ViewGroup.LayoutParams params = tv.getLayoutParams();
        params.height=(int) h;
        tv.setLayoutParams(params);
    }


    public void setText(Spanned text) {
        tv.setText(text);
        changeTv();
    }
}
