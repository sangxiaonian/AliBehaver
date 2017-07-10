package em.sang.com.allrecycleview.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/12/2 11:46
 */
public class SimpleHolder extends CustomPeakHolder {
    public SimpleHolder(View itemView) {
        super(itemView);
    }
    public SimpleHolder(Context context,int itemId) {
        super(LayoutInflater.from(context).inflate(itemId,null));
    }
}
