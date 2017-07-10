package em.sang.com.allrecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.holder.CustomPeakHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewLisenter;


/**
 * Description：带有头布局的Adapter
 *
 * @Author：桑小年
 * @Data：2016/11/8 9:50
 */
public class DefaultAdapter<T> extends CustomBasicAdapter {


    public DefaultAdapter(Context context, List<T> lists, int itemID, DefaultAdapterViewLisenter<T> lisenter) {
        super(context, lists, itemID, lisenter);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < tops.size()) {
            return TOP;
        } else if (position < heards.size()+tops.size()) {
            return position;
        } else if (position < heards.size() + lists.size() + tops.size()) {
            return BODY;
        } else if (position<heards.size() + lists.size() + tops.size()+foots.size()){
            return position;
        }else {
            return FOOT;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int id ;
        if (position < tops.size()) {
            id=position;
            ((CustomPeakHolder) holder).initView(id, context);
        } else if (position < heards.size()+tops.size()) {
            id= position-tops.size();
            ((CustomPeakHolder) holder).initView(id, context);
        } else if (position < heards.size() + lists.size() + tops.size()) {
            id=position-heards.size()-tops.size();
            ((CustomHolder) holder).initView(id,lists, context);
        } else if (position<heards.size() + lists.size() + tops.size()+foots.size()){
            id = position-heards.size()-tops.size()-lists.size();
            ((CustomPeakHolder) holder).initView(id, context);
        }else {
            id = position-(heards.size() + lists.size() + tops.size()+foots.size());
            ((CustomPeakHolder) holder).initView(id, context);
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TOP) {
            holder = (RecyclerView.ViewHolder) tops.get(0);
        } else if (viewType < heards.size()+tops.size()) {
            holder= (RecyclerView.ViewHolder) heards.get(viewType-tops.size());
        } else if (viewType == BODY) {
            holder= ((DefaultAdapterViewLisenter) listener).getBodyHolder(context, lists, itemID);
        } else if (viewType<heards.size() + lists.size() + tops.size()+foots.size()){
            holder= (RecyclerView.ViewHolder) foots.get(viewType - heards.size() - lists.size()-tops.size());
        }else if (viewType==FOOT){
            holder = (RecyclerView.ViewHolder) booms.get(0);
        }
        return holder;
    }
}
