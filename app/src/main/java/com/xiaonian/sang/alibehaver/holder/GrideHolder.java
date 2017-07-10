package com.xiaonian.sang.alibehaver.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.xiaonian.sang.alibehaver.R;
import com.xiaonian.sang.alibehaver.bean.GrideBean;

import java.util.List;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.holder.HeardHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewLisenter;

/**
 * 是时候撸点真正的代码了！！！
 * <p>
 * 创建人：桑小年
 * 日期：  2017/6/2.
 * <p>
 * 功能描述：
 */

public class GrideHolder<T> extends HeardHolder<T> {


    public GrideHolder(View itemView) {
        super(itemView);
    }

    public GrideHolder(List datas, View itemView) {
        super(datas, itemView);
    }

    public GrideHolder(Context context, List lists, int itemID) {
        super(context, lists, R.layout.recycleview_gride);
        this.itemID = itemID;
    }

    int span = 4;

    public void setSpan(int span) {
        this.span = span;
    }

    @Override
    public void initView(int position, Context context) {
        RecyclerView view = (RecyclerView) itemView.findViewById(R.id.rc_gv);
        GridLayoutManager manager = new GridLayoutManager(context, span);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(manager);

        view.setAdapter(new DefaultAdapter<T>(context, getDatas(), getItemID(), new DefaultAdapterViewLisenter<T>() {
            @Override
            public CustomHolder getBodyHolder(Context context, List<T> lists, int itemID) {
                return new CustomHolder<T>(context, lists, itemID) {
                    @Override
                    public void initView(final int position, List<T> datas, Context context) {
                        final T data = datas.get(position);
                        initItemView(itemView,data);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (GrideHolder.this.listener != null) {
                                    GrideHolder.this.listener.onItemClick(position, data);
                                }
                            }
                        });
                    }
                };
            }
        }));

    }

    public void initItemView(View itemView,T data){
        if (data instanceof GrideBean) {
            GrideBean bean = (GrideBean) data;
            TextView view = (TextView) itemView.findViewById(R.id.tv);
            if (bean.title != null) {
                view.setText(bean.title);
            }
            if (bean.imgId > 0) {
                Drawable drawable = ContextCompat.getDrawable(context, bean.imgId);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                view.setCompoundDrawables(null, drawable, null, null);
            }
        }
    }
}
