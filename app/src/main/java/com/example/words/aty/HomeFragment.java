package com.example.words.aty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.words.R;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * HomeFragment
 * Created by 6gold on 2017/3/4.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    /*成员变量--------------------------------------------------------*/
    View view;                                      //总UI构件

    //滚动banner所需包含的成员变量
    private ViewPager bannerViewPager;              //vp
    private ArrayList<ImageView> ivBannerImages;    //banner图片容器
    private ArrayList<View> vBannerDots;            //vBannerDots
    private int bannerCurrentItem;                  //当前banner页面的index，初始化为0
    private int bannerPreviousItem = 0;             //上次banner页面的index，初始化为0
    private int[] bannerImageID = new int[] {       //banner图片id
            R.mipmap.hb1,
            R.mipmap.hb2,
            R.mipmap.hb3
    };
    private String[] bannerTitles = new String[] {  //banner图片title
            "かんたんに立ち上がれる",
            "今度 目と目があったら 君に伝えよう",
            "てれくさくて すなおになれない"
    };
    private TextView tvBannerTitle;                             //banner-title的View
    private BannerViewPagerAdapter bannerViewPagerAdapter;      //vp适配器
    private ScheduledExecutorService scheduledExecutorService;  //线程池实现动画轮播

    //单词搜索框所需包含的成员变量
    private LinearLayout customSearchWordBox;

    /*相关函数--------------------------------------------------------*/

    /*@重写onCreateView()方法
     * 方法名：onCreate(......)
     * 功    能：创建活动
     * 参    数：......
     * 返回值：无
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_home,container,false);//为了获取view布局里面的控件

        /*初始化操作--------------------------------------------------------*/
        initViews();
        initEvents();
        initDatas();

        return view;        //返回view
    }

    /*
     * 方法名：initViews()
     * 功    能：初始化控件
     * 参    数：无
     * 返回值：无
     */
    private void initViews() {

        //初始化单词搜索框
        customSearchWordBox = (LinearLayout) view.findViewById(R.id.ll_custom_serchbox_static);

        //初始化ViewPager
        bannerViewPager = (ViewPager) view.findViewById(R.id.vp_hb);

        //初始化banner图片
        ivBannerImages = new ArrayList<ImageView>();
        for (int i = 0; i < bannerImageID.length; i++) {
            ImageView imageView = new ImageView(this.getContext());
            imageView.setBackgroundResource(bannerImageID[i]);
            ivBannerImages.add(imageView);
        }

        //初始化banner的dots
        vBannerDots = new ArrayList<View>();
        vBannerDots.add(view.findViewById(R.id.v_hb_dot_0));
        vBannerDots.add(view.findViewById(R.id.v_hb_dot_1));
        vBannerDots.add(view.findViewById(R.id.v_hb_dot_2));

        //初始化banner图片title
        tvBannerTitle = (TextView) view.findViewById(R.id.tv_hb_title);
        tvBannerTitle.setText(bannerTitles[0]);
    }

    /*
     * 方法名：initEvents()
     * 功    能：初始化事件(点击事件等)
     * 参    数：无
     * 返回值：无
     */
    private void initEvents() {
        customSearchWordBox.setOnClickListener(this);
    }

    /*
     * 方法名：initDatas()
     * 功    能：初始化数据(包括适配器的初始化)
     * 参    数：无
     * 返回值：无
     */
    private void initDatas() {
        //初始化banner-vp的适配器
        bannerViewPagerAdapter = new BannerViewPagerAdapter();
        bannerViewPager.setAdapter(bannerViewPagerAdapter);
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tvBannerTitle.setText(bannerTitles[position]);
                vBannerDots.get(position).setBackgroundResource(R.drawable.dot_focused);
                vBannerDots.get(bannerPreviousItem).setBackgroundResource(R.drawable.dot_normal);
                bannerCurrentItem = position;
                bannerPreviousItem = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });//tip：setOnPageChangeListener() is already deprecated
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*@重写onClick()方法
     * 方法名：onClick(View v)
     * 功    能：处理点击事件
     * 参    数：View v - 被点击的View
     * 返回值：无
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_custom_serchbox_static:    //点击搜索区域
            {
                Intent intent = new Intent(getActivity(),SearchWordActivity.class);
                intent.putExtra("haswordtocheck","noword");
                startActivity(intent);
                break;
            }
        }
    }

    /*@重写onStart()方法
     * 方法名：onStart()
     * 功    能：创建线程池执行动画轮播
     * 参    数：无
     * 返回值：无
     */
    @Override
    public void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS
        );
    }

    /*相关类--------------------------------------------------------*/
    //banner的ViewPager适配器
    public class BannerViewPagerAdapter extends PagerAdapter {

        @Override//返回banner的页卡数量
        public int getCount() {
            return ivBannerImages.size();
        }

        @Override//用于判断是否由对象生成界面???
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override //删除页卡
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(ivBannerImages.get(position));
        }

        @Override //添加页卡
        public Object instantiateItem (ViewGroup container, int position) {
            container.addView(ivBannerImages.get(position));
            return ivBannerImages.get(position);
        }
    }

    //图片轮播任务
    private class ViewPageTask implements Runnable {
        @Override
        public void run() {
            bannerCurrentItem = ( bannerCurrentItem + 1 ) % bannerImageID.length;
            hbHandler.sendEmptyMessage(0);
        }
    }

    //接收ViewPageTask子线程传过来的数据，完成UI更新操作
    public Handler hbHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            bannerViewPager.setCurrentItem(bannerCurrentItem);
        }
    };
}