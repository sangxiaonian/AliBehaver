package com.xiaonian.sang.alibehaver;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.sang.viewfractory.utils.ToastUtil;
import com.xiaonian.sang.alibehaver.bean.GrideBean;
import com.xiaonian.sang.alibehaver.holder.GrideHolder;
import com.xiaonian.sang.alibehaver.holder.HomeCarouselHolder;
import com.xiaonian.sang.alibehaver.holder.ItemHolder;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.RefrushRecycleView;
import em.sang.com.allrecycleview.adapter.RefrushAdapter;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewLisenter;
import em.sang.com.allrecycleview.inter.DefaultRefrushListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

import static android.drm.DrmStore.Playback.STOP;
import static java.lang.reflect.Modifier.NATIVE;

public class MainActivity extends AppCompatActivity implements OnToolsItemClickListener<GrideBean> {

    private RefrushRecycleView refrushRecycleView;
    private RefrushAdapter<String> adapter;
    private List<String> lists;
    private GrideHolder holder_up;
    private HomeCarouselHolder carouselHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToastUtil.init(this);
        initView();
    }

    private void initView() {
        refrushRecycleView = (RefrushRecycleView) findViewById(R.id.rc_home);
        lists=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add("ITEM"+i);
        }

        adapter = new RefrushAdapter<>(this, lists, R.layout.item, new DefaultAdapterViewLisenter() {
            @Override
            public CustomHolder getBodyHolder(Context context, List lists, int itemID) {
                return new ItemHolder(  context,   lists,   itemID);
            }
        });


        //显示下拉刷新
        refrushRecycleView.setHasTop(true);
        refrushRecycleView.setRefrushListener(new DefaultRefrushListener() {
            @Override
            public void onLoading() {
                super.onLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        refrushRecycleView.post(new Runnable() {
                            @Override
                            public void run() {
                                refrushRecycleView.loadSuccess();
                            }
                        });

                    }
                }).start();

            }
        });
        /**
         * 设置下拉刷新位置
         */
        adapter.setRefrushPosition(1);

        //上侧九宫格

        List<GrideBean> UP = new ArrayList<>();
        UP.add(new GrideBean(getString(R.string.phone), R.mipmap.phone_recharge));
        UP.add(new GrideBean(getString(R.string.life_recharge), R.mipmap.life_recharge));
        UP.add(new GrideBean(getString(R.string.traffic), R.mipmap.phone_recharge));
        UP.add(new GrideBean(getString(R.string.tick), R.mipmap.life_recharge));
        UP.add(new GrideBean(getString(R.string.game_recharge), R.mipmap.phone_recharge));
        UP.add(new GrideBean(getString(R.string.gas_recharge), R.mipmap.life_recharge));
        UP.add(new GrideBean(getString(R.string.lottery), R.mipmap.phone_recharge));
        UP.add(new GrideBean(getString(R.string.gongyi), R.mipmap.life_recharge));
        UP.add(new GrideBean(getString(R.string.share_media), R.mipmap.phone_recharge));
        UP.add(new GrideBean(getString(R.string.credit_payment), R.mipmap.life_recharge));
        UP.add(new GrideBean(getString(R.string.train), R.mipmap.phone_recharge));

        holder_up = new GrideHolder(this, UP, R.layout.item);
        holder_up.setOnTOnToolsItemClickListener(this);
        adapter.addHead(holder_up);


        List<GrideBean> list = new ArrayList<>();
        list.add(new GrideBean("", R.mipmap.home_banner1));
        list.add(new GrideBean("", R.mipmap.home_banner2));
        list.add(new GrideBean("", R.mipmap.home_banner3));
        list.add(new GrideBean("", R.mipmap.home_banner4));
        list.add(new GrideBean("", R.mipmap.home_banner5));

        //中间广告条
        carouselHolder = new HomeCarouselHolder(this, list, R.layout.item_home_carousel);
        adapter.addHead(carouselHolder);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        refrushRecycleView.setLayoutManager(manager);
        refrushRecycleView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position, GrideBean item) {
        ToastUtil.showTextToast(item.title);
    }
}
