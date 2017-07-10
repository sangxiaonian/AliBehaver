package em.sang.com.allrecycleview.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Description：头布局和叫布局使用的类
 *
 * @Author：桑小年
 * @Data：2016/11/7 16:46
 */
public  class CustomPeakHolder<T> extends CustomHolder {


    public CustomPeakHolder(View itemView) {
        super(itemView);
    }

    public CustomPeakHolder(List datas, View itemView) {
        super(datas, itemView);
    }

    public CustomPeakHolder(Context context, List lists, int itemID) {
        super(context, lists, itemID);
    }

    /**
     * 叫布局和头布局使用的方法
     * @param position
     * @param context
     */
    public void initView(int position,Context context) {

    }


    /**
     * 获取跟布局
     * @return
     */
    public View getItemView(){
        return  itemView;
    }





}
