package em.sang.com.allrecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sang.viewfractory.utils.JLog;

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
public class RefrushAdapter<T> extends DefaultAdapter<T> {

    private int refrushPosition=0;

    public RefrushAdapter(Context context, List lists, int itemID, DefaultAdapterViewLisenter lisenter) {
        super(context, lists, itemID, lisenter);
    }

    public void setRefrushPosition(int position){
        this.refrushPosition=position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == refrushPosition&&tops.size()>0) {
            return TOP;
        } else if (position < heards.size()+tops.size()) {
            if (position<refrushPosition) {
                return position;
            }else if (position>refrushPosition&&tops.size()>0){
                return position;
            }else {
                return position;
            }
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
        if (position == refrushPosition&&tops.size()>0) {
            id=position;
            ((CustomPeakHolder) holder).initView(id, context);
        } else if (position < heards.size()+tops.size()) {
            id= position-tops.size();
            if (position<refrushPosition) {
                id= position;
            }else if (position>refrushPosition&&tops.size()>0){
                id= position-1;
            }
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
            if (viewType>refrushPosition&&tops.size()>0) {
                holder = (RecyclerView.ViewHolder) heards.get(viewType - tops.size());
            }else {
                holder = (RecyclerView.ViewHolder) heards.get(viewType);
            }
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
