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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.words.R;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * MainActivity的HomeFragment子类
 * Created by 6gold on 2017/3/4.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    /*成员变量--------------------------------------------------------*/
    //滚动banner所需包含的成员变量
    private ViewPager hbViewPager;
    private ArrayList<ImageView> hbImages;
    private ArrayList<View> hbDots;
    private int hbCurrentItem;//当前banner点的位置，初始化为0
    private int hbPreviousItem = 0;//上一次banner点的位置，初始化为0
    private int[] hbImageID = new int[] {
            R.mipmap.hb1,
            R.mipmap.hb2,
            R.mipmap.hb3
    };//该数组存放banner图片的id
    private String[] hbTitles = new String[] {
            "かんたんに立ち上がれる",
            "今度 目と目があったら 君に伝えよう",
            "てれくさくて すなおになれない"
    };//该数组存放banner图片的title
    private TextView title;//界面中用于显示标题的View
    private HBViewPagerAdapter hbViewPagerAdapter;//vp适配器
    private ScheduledExecutorService scheduledExecutorService;//线程池实现动画轮播

    //单词搜索框所需包含的成员变量
    private LinearLayout customSearchBox;

    /*相关函数--------------------------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_home,container,false);//为了获取view布局里面的控件

        /*初始化操作--------------------------------------------------------*/
        //初始化topbar

        //初始化单词搜索框
        customSearchBox = (LinearLayout) view.findViewById(R.id.ll_custom_serchbox_static);
        customSearchBox.setOnClickListener(this);
        //初始化ViewPager
        hbViewPager = (ViewPager) view.findViewById(R.id.vp_hb);
        //初始化banner图片
        hbImages = new ArrayList<ImageView>();
        for (int i = 0; i < hbImageID.length; i++) {
            ImageView imageView = new ImageView(this.getContext());
            imageView.setBackgroundResource(hbImageID[i]);
            hbImages.add(imageView);
        }
        //初始化banner的小点
        hbDots = new ArrayList<View>();
        hbDots.add(view.findViewById(R.id.v_hb_dot_0));
        hbDots.add(view.findViewById(R.id.v_hb_dot_1));
        hbDots.add(view.findViewById(R.id.v_hb_dot_2));
        //初始化显示标题的TextView
        title = (TextView) view.findViewById(R.id.tv_hb_title);
        title.setText(hbTitles[0]);
        //初始化banner-vp的适配器
        hbViewPagerAdapter = new HBViewPagerAdapter();
        hbViewPager.setAdapter(hbViewPagerAdapter);
        hbViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                title.setText(hbTitles[position]);
                hbDots.get(position).setBackgroundResource(R.drawable.dot_focused);
                hbDots.get(hbPreviousItem).setBackgroundResource(R.drawable.dot_normal);
                hbCurrentItem = position;
                hbPreviousItem = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });//tip：setOnPageChangeListener已经过时了

        //返回view
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_custom_serchbox_static:
            {
                Intent intent = new Intent(getActivity(),SearchWordActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    //Class-滚动banner的ViewPager适配器
    public class HBViewPagerAdapter extends PagerAdapter {

        @Override//返回banner的页卡数量
        public int getCount() {
            return hbImages.size();
        }

        @Override//用于判断是否由对象生成界面???
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override //删除页卡
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(hbImages.get(position));
        }

        @Override //添加页卡
        public Object instantiateItem (ViewGroup container, int position) {
            container.addView(hbImages.get(position));
            return hbImages.get(position);
        }
    }

    //onStart方法，创建线程池执行动画轮播
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

    //图片轮播任务
    private class ViewPageTask implements Runnable {
        @Override
        public void run() {
            hbCurrentItem = ( hbCurrentItem + 1 ) % hbImageID.length;
            hbHandler.sendEmptyMessage(0);
        }
    }

    //接收ViewPageTask子线程传过来的数据，完成UI更新操作
    public Handler hbHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            hbViewPager.setCurrentItem(hbCurrentItem);
        }
    };
}
