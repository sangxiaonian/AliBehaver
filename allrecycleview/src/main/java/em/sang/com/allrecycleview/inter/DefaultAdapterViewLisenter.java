package em.sang.com.allrecycleview.inter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/11/8 16:10
 */
public class DefaultAdapterViewLisenter<T> implements CustomAdapterListener<T> {
    @Override
    public void initView(T data, View itemView) {

    }

    @Override
    public int getItemTypeByPosition() {
        return 0;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /**
     * 获取头布局holder
     * @param context 上下文
     * @param position  位置
     * @return holder
     */
    public CustomPeakHolder getHeardHolder(Context context, int position){return null;}
    /**
     * 获取普通布局holder
     * @param context 上下文
     * @param lists 数据
     * @param itemID 布局ID
     * @return holder
     */
    public CustomHolder getBodyHolder(Context context, List<T> lists, int itemID){return null;}
    /**
     * 获取脚局holder
     * @param context 上下文
     * @param position 脚布局位置,从0开始
     * @return holder
     */
    public CustomHolder getFootdHolder(Context context, int position){return null;}

    @Override
    public CustomHolder getHolderByViewType(Context context, List lists, int itemID) {
        return null;
    }
}
