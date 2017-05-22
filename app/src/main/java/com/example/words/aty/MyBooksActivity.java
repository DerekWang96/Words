package com.example.words.aty;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.words.R;
import java.util.ArrayList;

/**
 * Created by 6gold on 2017/5/22.
 */

public class MyBooksActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager vpMyBooks;                         //vp
    private FragmentPagerAdapter myBooksPagerAdapter;    //pa适配器

    //fragment切换按钮
    private Button btnMyBooks1;
    private Button btnMyBooks2;
    private Button btnMyBooks3;

    private ImageButton btnReturn;                       //返回按钮

    //装载Fragments
    private ArrayList<android.support.v4.app.Fragment> myBooksFragments;

    //三个fragment的layout
    private LinearLayout ll_mybooks1;
    private LinearLayout ll_mybooks2;
    private LinearLayout ll_mybooks3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybooks);

        initViews();
        initDatas();
        initEvents();

        resetButtons();
        selectTab(0);
    }

    public void initViews()
    {
        vpMyBooks = (ViewPager) findViewById(R.id.vp_mybooks);

        btnMyBooks1 = (Button) findViewById(R.id.btn_mybooks_tab1);
        btnMyBooks2 = (Button) findViewById(R.id.btn_mybooks_tab2);
        btnMyBooks3 = (Button) findViewById(R.id.btn_mybooks_tab3);

        btnReturn = (ImageButton) findViewById(R.id.btn_return_mybooks);

        ll_mybooks1 = (LinearLayout) findViewById(R.id.ll_fragment_mybooks1);
        ll_mybooks2 = (LinearLayout) findViewById(R.id.ll_fragment_mybooks2);
        ll_mybooks3 = (LinearLayout) findViewById(R.id.ll_fragment_mybooks3);
    }

    public void initEvents()
    {
        //设置tab按钮点击事件
        btnMyBooks1.setOnClickListener(this);
        btnMyBooks2.setOnClickListener(this);
        btnMyBooks3.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
    }

    public void initDatas()
    {
        myBooksFragments = new ArrayList<>();
        //将三个Fragment加入集合
        myBooksFragments.add(new MyBooks1Fragment());
        myBooksFragments.add(new MyBooks2Fragment());
        myBooksFragments.add(new MyBooks3Fragment());

        //初始化适配器
        myBooksPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                //从集合中获取对应位置的Fragment
                return myBooksFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return myBooksFragments.size();
            }
        };
        //设置vp适配器
        vpMyBooks.setAdapter(myBooksPagerAdapter);

        vpMyBooks.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            //页面选中事件
            public void onPageSelected(int position) {
                vpMyBooks.setCurrentItem(position);
                resetButtons();//按钮底色全部置浅色，上面的字全部置深色
                selectTab(position);//被选中按钮底色置深色，上面的字置浅色
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_mybooks_tab1:
            {
                resetButtons();
                selectTab(0);
                break;
            }
            case R.id.btn_mybooks_tab2:
            {
                resetButtons();
                selectTab(1);
                break;
            }
            case R.id.btn_mybooks_tab3:
            {
                resetButtons();
                selectTab(2);
                break;
            }
            case R.id.btn_return_mybooks:
            {
                finish();
                break;
            }
        }
    }

    public void resetButtons()
    {
        btnMyBooks1.setBackgroundColor(Color.parseColor("#f8f8f8"));
        btnMyBooks2.setBackgroundColor(Color.parseColor("#f8f8f8"));
        btnMyBooks3.setBackgroundColor(Color.parseColor("#f8f8f8"));
        btnMyBooks1.setTextColor(Color.parseColor("#333333"));
        btnMyBooks2.setTextColor(Color.parseColor("#333333"));
        btnMyBooks3.setTextColor(Color.parseColor("#333333"));
    }

    public void selectTab(int position)
    {
        switch (position)
        {
            case 0:
                btnMyBooks1.setBackgroundColor(Color.parseColor("#333333"));
                btnMyBooks1.setTextColor(Color.parseColor("#f8f8f8"));
                break;
            case 1:
                btnMyBooks2.setBackgroundColor(Color.parseColor("#333333"));
                btnMyBooks2.setTextColor(Color.parseColor("#f8f8f8"));
                break;
            case 2:
                btnMyBooks3.setBackgroundColor(Color.parseColor("#333333"));
                btnMyBooks3.setTextColor(Color.parseColor("#f8f8f8"));
                break;
        }
        vpMyBooks.setCurrentItem(position);
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

/*
* 注意
* ①这里要导入的是import android.support.v4.app.Fragment;的fragment
*   而不是android.app.Fragment
*   因为ViewPager是import android.support.v4.app.Fragment
* */
