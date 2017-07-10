package com.sang.viewfractory.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.sang.viewfractory.R;
import com.sang.viewfractory.utils.DeviceUtils;
import com.sang.viewfractory.utils.ViewUtils;


/**
 * 作者： ${桑小年} on 2016/5/22.
 * 努力，为梦长留
 */
public class HorizontalProgress extends ProgressBar {


    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;
    private  int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR ;
    private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;
    private static final int DEFAULT_SIZE_TEXT_OFFSET = 10;

    public Paint mPaint =new Paint();

    /**
     * 到达的进度条颜色
     */
    public int recarchColor;
    /**
     * 没有到达的进度条颜色
     */
    public int unRecarchColor;

    /**
     * 字体大小
     */
    public int textSize;

    /**
     * 字体颜色
     */
    public int textColor;

    /**
     * 进度条高度
     */
    public int mHeight;

    /**
     * 字体两边的距离
     */
    public int textOffset;

    /**
     * 是否显示字体
     */
    public int textVisible ;


    public boolean ifDrawText=true;

    /**控件宽度*/
    public int mRealWidth;

    public boolean ifRech =false;
    public int textWidth;

    public HorizontalProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setHorizontalScrollBarEnabled(true);

        initView(context, attrs, defStyleAttr);

        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
    }

    /**
     * 初始化属性
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        DEFAULT_HEIGHT_REACHED_PROGRESS_BAR= DeviceUtils.dip2px(context,2);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgress);
        textColor = typedArray.getColor(R.styleable.HorizontalProgress_progress_text_color, DEFAULT_TEXT_COLOR);


        recarchColor = typedArray.getColor(R.styleable.HorizontalProgress_progress_reached_color, textColor);
        unRecarchColor =typedArray.getColor(R.styleable.HorizontalProgress_progress_unreached_color,DEFAULT_COLOR_UNREACHED_COLOR);
        textSize = (int) typedArray.getDimension(R.styleable.HorizontalProgress_progress_text_size,DEFAULT_TEXT_SIZE);
        textOffset = (int) typedArray.getDimension(R.styleable.HorizontalProgress_progress_text_offset,textOffset);
        mHeight = (int) typedArray.getDimension(R.styleable.HorizontalProgress_progress_height,DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);
        textVisible=typedArray.getInt(R.styleable.HorizontalProgress_progress_text_visibility,VISIBLE);

        if (textVisible!=VISIBLE){
            ifDrawText=false;
        }


        typedArray.recycle();
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //在用户没有指定进度条高度的情况下，使用我们自定义的高度，否则就直接使用用户指定的高度了
        if (heightMode!=MeasureSpec.EXACTLY){
            //获取文本高度
            float textHeighe = mPaint.ascent()+mPaint.descent();
            //获取进度条高度，取文本和进度条之间的最大值
            float ecptHeitht = getPaddingTop()+getPaddingBottom()+Math.max(textHeighe,mHeight);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) ecptHeitht,MeasureSpec.EXACTLY);
        }

        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();


        //将画笔移动到控件最左侧{getpaddintLeft，getHeight（）/2}，以此作为基础（0，0）
        canvas.translate(getPaddingLeft(),getHeight()/2);

        //获取当前进度
        float radio = getProgress()*1.0f/getMax();



        mRealWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-textWidth;
        //已经到达的宽度
        float progressX = (mRealWidth*radio);


        //如果到达最后，则不再绘制未到达的进度
//        if (progressX+ textWidth >mRealWidth){
//            progressX = mRealWidth - textWidth;
//            ifRech=true;
//        }
        if (progressX >mRealWidth){
            progressX = mRealWidth ;
            ifRech=true;
        }

        //绘制已经到达的进度
        float endX = progressX-textOffset/2;
        if (endX>0) {
            mPaint.setColor(recarchColor);
            mPaint.setStrokeWidth(mHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }
        if (ifDrawText){
            mPaint.setColor(textColor);
            //需要画出的文本
            String text = ViewUtils.get2Double(radio * 100) + "%";


            //文字的宽高
            textWidth = (int) mPaint.measureText(text);

            int textHeight = (int) (mPaint.descent() + mPaint.ascent());
            //绘制文本
            canvas.drawText(text,progressX,-textHeight/2,mPaint);
        }


        //绘制尚未到达的进度条
        if (!ifRech){
            mPaint.setColor(unRecarchColor);
            mPaint.setStrokeWidth(mHeight);
            canvas.drawLine(progressX+textOffset/2+ textWidth,0,mRealWidth+textWidth,0,mPaint);
        }

        canvas.restore();



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }
}
