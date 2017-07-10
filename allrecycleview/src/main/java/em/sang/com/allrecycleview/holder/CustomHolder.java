package em.sang.com.allrecycleview.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/11/7 16:46
 */
public  class CustomHolder<T> extends RecyclerView.ViewHolder {


    public Context context;
    public View itemView;
    protected List<T> datas;
    protected int itemID;
    public OnToolsItemClickListener<T> listener;
    public void setOnTOnToolsItemClickListener(OnToolsItemClickListener<T> listener){
        this.listener=listener;
    }


    public CustomHolder(View itemView) {
        super(itemView);
    }

    public CustomHolder(List<T> datas, View itemView ){
        this(itemView);
        this.datas = datas;
        this.itemView = itemView;

    }

    public CustomHolder(Context context, List<T> lists, int itemID) {
        this(lists,LayoutInflater.from(context).inflate(itemID,null));
        this.context =context;
        this.itemID=itemID;
    }

    /**
     * body中使用的初始化方法
     * @param position
     * @param datas
     * @param context
     */
    public void initView(int position,List<T> datas,Context context) {

    }




    /**
     * 获取跟布局
     * @return
     */
    public View getItemView(){
        return  itemView;
    }

    public Context getContext() {
        return context;
    }

    public List<T> getDatas() {
        return datas;
    }

    public int getItemID() {
        return itemID;
    }
}
