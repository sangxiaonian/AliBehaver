package em.sang.com.allrecycleview;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 *
 * 是时候撸点真正的代码了！！！
 *
 * 创建人：桑小年
 * 日期：  2017/6/2
 *
 * 功能描述：基础的类，包涵一些基本的功能
 */

public class BaiscRecycleView extends RecyclerView {
    public BaiscRecycleView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public BaiscRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public BaiscRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }
    protected void initView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    }

    public boolean isFirst() {
        LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) manager);
            int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (position == -1) {
                position = 0;
            }
            return 0 == position  ;
        } else if (manager instanceof StaggeredGridLayoutManager) {

            int position = 0;
            try {
                position = ((StaggeredGridLayoutManager) manager).findFirstVisibleItemPositions(new int[2])[0];
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (position <= 0) {
                position = 0;
            }
            return 0 == position ;
        } else {
            return false;
        }
    }

    public boolean isLast() {
        int itemCount = getAdapter().getItemCount() - 1;
        LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) manager);
            return itemCount == linearLayoutManager.findLastVisibleItemPosition()  ;
        } else if (manager instanceof StaggeredGridLayoutManager) {
            int i = 0;
            try {
                i = ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(new int[2])[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemCount == i;

        } else {
            return false;
        }
    }



}
