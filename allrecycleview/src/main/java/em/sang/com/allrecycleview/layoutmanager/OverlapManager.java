package em.sang.com.allrecycleview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.sang.viewfractory.utils.Apputils;


/**
 * Description：
 *
 * @ Author ：桑小年
 * @ Data ：2016/12/22 15:52
 */
public class OverlapManager extends LinearLayoutManager {

    private int offSet;

    public OverlapManager(Context context) {
        super(context);
        initView(context);
    }

    public OverlapManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        initView(context);
    }

    public OverlapManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        offSet = Apputils.dip2px(context, 5);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        detachAndScrapAttachedViews(recycler);
        int childCount = getItemCount();
        for (int i = 0; i < childCount; i++) {
            View child = recycler.getViewForPosition(i);
            measureChildWithMargins(child, 0, 0);
            int childWidth = getDecoratedMeasuredWidth(child);
            int childHeight = getDecoratedMeasuredHeight(child);
            int startX = (getWidth() - childWidth) / 2;
            int startY = (getHeight() - childHeight) / 2;

            if (i < childCount - 1) {
                child.setScaleX(0.8f);
                child.setScaleY(0.8f);
            }
            addView(child);
            layoutDecorated(child, startX, startY, startX + childWidth, startY + childHeight);
            if (i == childCount - 1) {
                child.setTranslationY(0);
            } else if (i == childCount - 2) {
                child.setScaleX(0.9f);
                child.setScaleY(0.9f);
                child.setTranslationY(childHeight * 0.1f / 2 + offSet);
            } else if (i == childCount - 3) {
                child.setScaleX(0.8f);
                child.setScaleY(0.8f);
                child.setTranslationY(childHeight * 0.2f / 2 + offSet * 2);
            }


        }
    }

}
