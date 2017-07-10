package em.sang.com.allrecycleview.inter;

import android.content.Context;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

public interface BodyInitListener<T> {

    public CustomHolder getHolderByViewType(Context context, List<T> lists, int itemID);
}
