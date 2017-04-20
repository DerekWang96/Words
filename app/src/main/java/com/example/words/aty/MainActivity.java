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
    private ViewPager myViewPager;                              //vp
    private FragmentPagerAdapter myViewPagerAdapter;            //vp适配器
    private List<Fragment> myViewPagerFragments;                //vp存放的Fragment集合
    private LinearLayout tabBooks, tabHome, tabPersonal;        //Fragments对应布局
    private ImageButton imgBtnBooks, imgBtnHome, imgBtnPersonal;//Fragments对应按钮

    /*相关函数--------------------------------------------------------*/
    /*@重写onCreate()方法
     * 方法名：onCreate(Bundle savedInstanceState)
     * 功    能：创建活动
     * 参    数：Bundle savedInstanceState - ?
     * 返回值：无
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去掉头部
        setContentView(R.layout.activity_main);         //设置layotu
        initViews();    //初始化控件
        initEvents();   //初始化事件
        initDatas();    //初始化数据

        //初始化完成之后，先将三个button置浅灰色，然后默认选择tab1(即主页)，对应的图标高亮
        resetImgs();
        selectTab(1);
    }

    /*
     * 方法名：initViews()
     * 功    能：初始化控件
     * 参    数：无
     * 返回值：无
     */
    private void initViews() {
        myViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        tabBooks = (LinearLayout) findViewById(R.id.id_tab_books);
        tabHome = (LinearLayout) findViewById(R.id.id_tab_home);
        tabPersonal = (LinearLayout) findViewById(R.id.id_tab_personal);

        imgBtnBooks = (ImageButton) findViewById(R.id.id_tab_books_img);
        imgBtnHome = (ImageButton) findViewById(R.id.id_tab_home_img);
        imgBtnPersonal = (ImageButton) findViewById(R.id.id_tab_personal_img);
    }

    /*
     * 方法名：initEvents()
     * 功    能：初始化事件(点击事件等)
     * 参    数：无
     * 返回值：无
     */
    private void initEvents() {
        //设置底栏三个tab的点击事件
        tabBooks.setOnClickListener(this);
        tabHome.setOnClickListener(this);
        tabPersonal.setOnClickListener(this);
    }

    /*
     * 方法名：initDatas()
     * 功    能：初始化数据(包括适配器的初始化)
     * 参    数：无
     * 返回值：无
     */
    private void initDatas() {
        myViewPagerFragments = new ArrayList<>();
        //将各个Fragment加入集合中
        myViewPagerFragments.add(new BooksFragment());
        myViewPagerFragments.add(new HomeFragment());
        myViewPagerFragments.add(new PersonalFragment());

        //初始化vp适配器 - myViewPagerAdapter
        myViewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override //获取页面i
            public Fragment getItem(int position) {
                return myViewPagerFragments.get(position);
            }

            @Override //获取页面总数
            public int getCount() {
                return myViewPagerFragments.size();
            }
        };
        //设置适配器
        myViewPager.setAdapter(myViewPagerAdapter);

        //设置vp的切换监听
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override //页面选中事件
            public void onPageSelected(int position) {
                resetImgs();            //重置底栏图标
                selectTab(position);    //选择一个页面显示
            }

            @Override //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {}
        });
    }

    /*@重写onClick()方法
     * 方法名：onClick(View v)
     * 功    能：处理按钮点击事件
     * 参    数：View v - 按钮的View
     * 返回值：无
     */
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

    /*
     * 方法名：selectTab(int i)
     * 功    能：选择底栏Tab对应的页面
     * 参    数：int i - 所选择页面的index
     * 返回值：无
     */
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
        myViewPager.setCurrentItem(i);
    }

    /*
     * 方法名：resetImgs()
     * 功    能：将三个Button设置为浅灰色
     * 参    数：无
     * 返回值：无
     */
    private void resetImgs() {
        imgBtnBooks.setImageResource(R.mipmap.tab_book_normal);
        imgBtnHome.setImageResource(R.mipmap.tab_home_normal);
        imgBtnPersonal.setImageResource(R.mipmap.tab_personal_normal);
    }

    /*
     * @重写aty生命周期中的其它几个函数
     */
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