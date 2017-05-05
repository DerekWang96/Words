package com.example.words.aty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.words.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 6gold on 2017/5/5.
 */


//查看单词本内的单词，ACID类里面有个getwordfromwb函数
public class MyBooksActivity extends Activity implements View.OnClickListener {

    private ViewPager myViewPager;
    private FragmentPagerAdapter myViewPagerAdapter;
    private List<Fragment> myFragments;
    private LinearLayout tab1, tab2, tab3;
    private Button btnTab1, btnTab2, btnTab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mybooks);

        initViews();
        intiEvents();
        intiDatas();
        selectTab(0);
    }

    /*
     * 方法名：initViews()
     * 功    能：初始化控件
     * 参    数：无
     * 返回值：无
     */
    public void initViews() {
        myViewPager = (ViewPager) findViewById(R.id.vp_mybooks);

        tab1 = (LinearLayout) findViewById(R.id.ll_fragment_mybooks1);
        tab2 = (LinearLayout) findViewById(R.id.ll_fragment_mybooks2);
        tab3 = (LinearLayout) findViewById(R.id.ll_fragment_mybooks3);

        btnTab1 = (Button) findViewById(R.id.btn_mybooks_tab1);
        btnTab2 = (Button) findViewById(R.id.btn_mybooks_tab2);
        btnTab3 = (Button) findViewById(R.id.btn_mybooks_tab3);
    }

    /*
     * 方法名：initEvents()
     * 功    能：初始化事件(点击事件等)
     * 参    数：无
     * 返回值：无
     */
    public void intiEvents() {
        btnTab1.setOnClickListener(this);
        btnTab2.setOnClickListener(this);
        btnTab3.setOnClickListener(this);
    }

    /*
     * 方法名：initDatas()
     * 功    能：初始化数据(包括适配器的初始化)
     * 参    数：无
     * 返回值：无
     */
    public void intiDatas() {
        myFragments = new ArrayList<>();
        myFragments.add(new Mybook1Fragment());
        myFragments.add(new Mybook2Fragment());
        myFragments.add(new Mybook3Fragment());

        //初始化vp的adapter
        myViewPagerAdapter = new FragmentPagerAdapter() {
            @Override
            public Fragment getItem(int position) {
                return myFragments.get(position);
            }

            @Override
            public int getCount() {
                return myFragments.size();
            }
        }

        //设置vp的adapter
        myViewPager.setAdapter(myViewPagerAdapter);
        //设置vp的切换监听
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                resetButtons();//重置三个按钮
                selectTab(position);//选择一个tab显示
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
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
            case R.id.btn_mybooks_tab1:
                resetButtons();//先将三个button置浅灰色
                selectTab(0);//再选择tab0，对应的button高亮
                break;
            case R.id.btn_mybooks_tab2:
                resetButtons();//先将三个button置浅灰色
                selectTab(1);//再选择tab1，对应的button高亮
                break;
            case R.id.btn_mybooks_tab3:
                resetButtons();//先将三个button置浅灰色
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
    public void selectTab(int i) {
        //根据点击的Tab设置对应的Button为主题色
        switch (i) {
            case 0:
                btnTab1.setBackgroundColor(R.color.colorPrimaryLight);
                btnTab1.setTextColor(R.color.colorTextDark);
                break;
            case 1:
                btnTab2.setBackgroundColor(R.color.colorPrimaryLight);
                btnTab2.setTextColor(R.color.colorTextDark);
                break;
            case 2:
                btnTab3.setBackgroundColor(R.color.colorPrimaryLight);
                btnTab3.setTextColor(R.color.colorTextDark);
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
    private void resetButtons() {
        btnTab1.setTextColor(R.color.colorPrimaryLight);
        btnTab1.setBackgroundColor(R.color.colorTextDark);
        btnTab2.setTextColor(R.color.colorPrimaryLight);
        btnTab2.setBackgroundColor(R.color.colorTextDark);
        btnTab3.setTextColor(R.color.colorPrimaryLight);
        btnTab3.setBackgroundColor(R.color.colorTextDark);
    }

    //自定义myBooksFragmentAdapter类
//    public class BooksFragmentAdapter extends FragmentPagerAdapter {
//        ArrayList<Fragment> booksFragments;
//        public BooksFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
//            super(fm);
//            this.booksFragments = list;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return booksFragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return booksFragments.size();
//        }
//    };


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
