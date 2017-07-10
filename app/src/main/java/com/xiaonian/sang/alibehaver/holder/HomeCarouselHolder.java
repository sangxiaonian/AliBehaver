package com.xiaonian.sang.alibehaver.holder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sang.viewfractory.utils.Apputils;
import com.xiaonian.sang.alibehaver.R;
import com.xiaonian.sang.alibehaver.bean.GrideBean;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * Description：首页轮播图
 *
 * @Author：桑小年
 * @Data：2017/1/3 11:17
 */
public class HomeCarouselHolder extends CustomPeakHolder {

    ViewPager vp;
    LinearLayout ll_tag;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int count = vp.getCurrentItem() + 1;
            if (count >= vp.getAdapter().getCount()) {
                count = 0;
            }

            try {
                vp.setCurrentItem(count);
            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.sendEmptyMessageDelayed(0, 5 * 1000);
        }
    };

    public HomeCarouselHolder(Context context, List lists, int itemID) {
        super(context, lists, itemID);

    }



    CarousAdapter adapter;
//    @Override
    public void initView(int position, Context context) {

        if (datas == null || datas.size() == 0) {
            return;
        }
        vp = (ViewPager) itemView.findViewById(R.id.vp_home_carousel);
        ll_tag = (LinearLayout) itemView.findViewById(R.id.ll_home_tag);
        ll_tag.removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            ImageView tag = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, Apputils.dip2px(context, 7), 0);
            tag.setLayoutParams(params);
            tag.setImageResource(R.drawable.tag_carousel);
            ll_tag.addView(tag);
        }
        adapter = new CarousAdapter();
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < ll_tag.getChildCount(); i++) {
                    if (i == position) {
                        ll_tag.getChildAt(i).setEnabled(true);
                    } else {
                        ll_tag.getChildAt(i).setEnabled(false);
                    }

                }
            }
        });

        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        stopCarousel();
                        break;
                    default:
                        startCarousel();
                        break;

                }

                return false;
            }
        });
        for (int i = 0; i < ll_tag.getChildCount(); i++) {
            if (i == vp.getCurrentItem()) {
                ll_tag.getChildAt(i).setEnabled(true);
            } else {
                ll_tag.getChildAt(i).setEnabled(false);
            }

        }

        startCarousel();

    }

    public void stopCarousel() {
        if (handler != null && handler.hasMessages(0)) {
            handler.removeMessages(0);
        }
    }

    public void startCarousel() {
        if (handler != null) {
            if (handler.hasMessages(0)) {
                handler.removeMessages(0);
            }
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    }


    public class CarousAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);

            imageView.setImageResource(((List<GrideBean>)datas).get(position).imgId);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            imageView.setLayoutParams(layoutParams);
//            final Set_Item item= (Set_Item) datas.get(position);
//
//            if (!TextUtils.isEmpty(item.img_url)) {
//                GlideUtils.loadImage(context,imageView,item.img_url);
//            }else {
//                GlideUtils.loadImage(context,imageView,item.icon_id);
//
//            }
            imageView.setEnabled(true);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!TextUtils.isEmpty(item.describe)){
//                        Intent intent = new Intent(context, ShowDetailActivity.class);
//                        TranInfor infor = new TranInfor();
//                        infor.title = item.title;
//                        infor.type = 1;
//                        infor.content = item.describe;
//                        infor.describe = item.describe;
//                        intent.putExtra(Config.infors, infor);
//                       context. startActivity(intent);
//                    }
//                }
//            });

            container.addView(imageView);
            return imageView;
        }
    }

}
