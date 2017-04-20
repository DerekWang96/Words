package com.example.words.aty;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.words.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 主Activity
 * 由LoginActivity启动（登录成功后要进入的活动）
 * Created by 6gold on 2017/2/28.
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    /*成员变量--------------------------------------------------------*/
    //banner所需包含的成员变量

    private ViewPager homeViewPager;//声明ViewPager
    private FragmentPagerAdapter homeViewPagerAdapter;//适配器
    private List<Fragment> homeViewPagerFragments;//装载Fragment的集合

    //三个tab对应的布局
    private LinearLayout tabBooks;
    private LinearLayout tabHome;
    private LinearLayout tabPersonal;

    //三个Tab对应的ImageButton
    private ImageButton imgBtnBooks;
    private ImageButton imgBtnHome;
    private ImageButton imgBtnPersonal;


    /*相关函数--------------------------------------------------------*/
    //onCreate函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据

        //初始化完成之后，先将三个button置浅灰色，然后默认选择tab1(即主页)，同时将对应的图标高亮
        resetImgs();
        selectTab(1);
    }

    //初始化控件
    private void initViews() {
        homeViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        tabBooks = (LinearLayout) findViewById(R.id.id_tab_books);
        tabHome = (LinearLayout) findViewById(R.id.id_tab_home);
        tabPersonal = (LinearLayout) findViewById(R.id.id_tab_personal);

        imgBtnBooks = (ImageButton) findViewById(R.id.id_tab_books_img);
        imgBtnHome = (ImageButton) findViewById(R.id.id_tab_home_img);
        imgBtnPersonal = (ImageButton) findViewById(R.id.id_tab_personal_img);
    }

    //初始化事件
    private void initEvents() {
        //设置底栏三个tab的点击事件
        tabBooks.setOnClickListener(this);
        tabHome.setOnClickListener(this);
        tabPersonal.setOnClickListener(this);
    }

    //初始化数据
    private void initDatas() {
        homeViewPagerFragments = new ArrayList<>();
        //将各个Fragment加入集合中
        homeViewPagerFragments.add(new BooksFragment());
        homeViewPagerFragments.add(new HomeFragment());
        homeViewPagerFragments.add(new PersonalFragment());

        //设置主页切换ViewPager的适配器（利用前面声明的Fragment组）
        homeViewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return homeViewPagerFragments.get(position);//从集合中获取对应位置的Fragment
            }

            @Override
            public int getCount() {
                return homeViewPagerFragments.size();//获取集合中Fragment的总数
            }
        };
        homeViewPager.setAdapter(homeViewPagerAdapter);//设置适配器

        //设置homeViewPager的切换监听
        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override//页面选中事件
            public void onPageSelected(int position) {
                homeViewPager.setCurrentItem(position);
                resetImgs();//重置底栏图标
                selectTab(position);//选择一个fragment显示
            }

            @Override//页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override//页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {}
        });
    }

    //onClick监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //根据点击的Tab切换不同的页面以及对应的ImageButton为主题色
            case R.id.id_tab_books:
                resetImgs();//先将三个button置浅灰色
                selectTab(0);//再选择tab0，对应的button高亮
                break;
            case R.id.id_tab_home:
                resetImgs();//先将三个button置浅灰色
                selectTab(1);//再选择tab1，对应的button高亮
                break;
            case R.id.id_tab_personal:
                resetImgs();//先将三个button置浅灰色
                selectTab(2);//再选择tab2，对应的button高亮
                break;
            //......
        }
    }

    //选择底栏Tab对应的页面
    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为主题色
        switch (i) {
            case 0:
                imgBtnBooks.setImageResource(R.mipmap.tab_book);
                break;
            case 1:
                imgBtnHome.setImageResource(R.mipmap.tab_home);
                break;
            case 2:
                imgBtnPersonal.setImageResource(R.mipmap.tab_personal);
                break;
        }
        //设置当前点击的Tab所对应的Fragment为Fragment数组里的第i项
        homeViewPager.setCurrentItem(i);
    }

    //将三个Button设置为浅灰色
    private void resetImgs() {
        imgBtnBooks.setImageResource(R.mipmap.tab_book_normal);
        imgBtnHome.setImageResource(R.mipmap.tab_home_normal);
        imgBtnPersonal.setImageResource(R.mipmap.tab_personal_normal);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}