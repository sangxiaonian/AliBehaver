package em.sang.com.allrecycleview.holder;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/11/8 11:58
 */
public class FootHolder<T> extends CustomPeakHolder<T> {


    public FootHolder(View itemView) {
        super(itemView);
    }

    public FootHolder(List datas, View itemView) {
        super(datas, itemView);
    }

    public FootHolder(Context context, List lists, int itemID) {
        super(context, lists, itemID);
    }
}
