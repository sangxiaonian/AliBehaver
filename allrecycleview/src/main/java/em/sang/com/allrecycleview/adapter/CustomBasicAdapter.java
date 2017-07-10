package em.sang.com.allrecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.holder.CustomPeakHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewLisenter;


/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/11/7 16:43
 */
public class CustomBasicAdapter<T> extends BasicAdapter {
    protected CustomBasicAdapter(Context context, List<T> lists, int itemID, DefaultAdapterViewLisenter<T> lisenter) {
        super(context, lists, itemID, lisenter);
    }

    @Override
    public int getItemViewType(int position) {
        if (listener != null) {
            return listener.getItemTypeByPosition();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CustomHolder holder = null;
        if (listener!=null) {
            holder   = listener.getHolderByViewType(context, lists, itemID);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < heards.size()) {
            ((CustomHolder) holder).initView(position,lists,context);

        } else if (position < heards.size() + lists.size()) {
            ((CustomPeakHolder) holder).initView(position - heards.size(),context);

        } else {
            ((CustomPeakHolder) holder).initView(position - heards.size() - lists.size(),context);
        }

    }



}
