package com.example.words.aty;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.R;
import com.example.words.calendar.CalendarCard;
import com.example.words.calendar.CalendarViewAdapter;
import db.Custom;
import com.example.words.calendar.CustomDate;
import com.example.words.calendar.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CalendarTestActivity extends Activity implements CalendarCard.OnCellClickListener, View.OnClickListener {

    private TextView tvTitle;

    private ViewPager vpCalendar;
    private TextView tvMonth;
    private TextView tvTotalDays;
    private ImageView ivSignSucceed;
    private int mCurrentIndex = 498;
    private CalendarViewAdapter<CalendarCard> calendarViewAdapter;
    private List<Custom> listDay;
    private CalendarCard[] views;
    private SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
    private LinearLayout indicatorLayout;
    private Button btnSignToday;
    private ImageButton btnReturn;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE
    }
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar_test);

        initViews();
        initEvents();
        initData();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_topbar_title);
        tvTitle.setText("签到");

        vpCalendar = (ViewPager) findViewById(R.id.vp_calendar);
        tvMonth = (TextView) findViewById(R.id.tv_current_month);
        tvTotalDays = (TextView) findViewById(R.id.tv_total_sign_days);
        ivSignSucceed = (ImageView) findViewById(R.id.iv_sign_succeed);
        indicatorLayout = (LinearLayout) findViewById(R.id.ll_calendar_drop);
        btnSignToday = (Button) findViewById(R.id.btn_sign);
        btnReturn = (ImageButton) findViewById(R.id.btn_return);

        int month = DateUtil.getCurrentMonthNow();
        int year = DateUtil.getCurrentYeatNow();
        CustomDate c = new CustomDate(year, month, 1);
        tvMonth.setText(showTimeCount(c));

        // 若今日已经签到，则使签到按钮失效
        if (false) {
            btnSignToday.setEnabled(false);
        }
    }

    public void initEvents() {
        btnSignToday.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
    }

    private void initData() {

        // 初始化已经签到的日期，数据从本地读取or联网读取
        listDay = new ArrayList<>();
        for (int i = 1;i<10;i++){
            Custom custom = new Custom(2017,10,i);   //假设2017年10月1~9日均签到
            listDay.add(custom);
        }

        views = new CalendarCard[6];
        for (int i = 0; i < 6; i++) {
            views[i] = new CalendarCard(this, this, listDay);
        }
        calendarViewAdapter = new CalendarViewAdapter<>(views);
        setViewPager();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_sign:     // 今日签到
                // 若联网成功
                if (true) {
                    // 获取年月日发给服务器
                    Custom e = new Custom(DateUtil.getYear(),DateUtil.getMonth(),DateUtil.getCurrentMonthDay());
                    // Toast
                    Toast.makeText(this,"签到成功！",Toast.LENGTH_SHORT).show();
                    // 天数加1
                    int oldTotalDays = Integer.valueOf(tvTotalDays.getText().toString());
                    int newTotalDays = oldTotalDays+1;
                    System.out.println("原天数："+oldTotalDays+"；新天数："+newTotalDays);
                    tvTotalDays.setText(String.valueOf(newTotalDays));
                    // 更改按钮状态
                    btnSignToday.setText("今日已签到 √");
                    btnSignToday.setEnabled(false);
//                    ivSignSucceed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_return:
                finish();
                break;
        }
    }

    @Override
    public void clickDate(CustomDate date) {
        Toast.makeText(this,showTimeCountAll(date),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeDate(CustomDate date) {

    }

    /*
     * 设置vpCalendar
     */
    private void setViewPager() {
        vpCalendar.setAdapter(calendarViewAdapter);
        vpCalendar.setCurrentItem(mCurrentIndex);
        vpCalendar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);     //检测滑动方向
                updateCalendarView(position);   //更新CalendarCard的视图
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /*
     * 计算滑动方向
     */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    /*
     * 更新日历视图
     */
    private void updateCalendarView(int arg0) {
        CustomDate customDate = new CustomDate();
        CalendarCard[] mShowViews = calendarViewAdapter.getAllItems();  //获取所有CalendarCard
        if (mDirection == SildeDirection.RIGHT) {
            customDate = mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            customDate = mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
        if (customDate != null) {
            tvMonth.setText(showTimeCount(customDate));
            //进行网络请求

        }
    }

    public String showTimeCount(CustomDate time) {
        String timeCount;
        long minuec = time.month;
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());
        long secc = time.day;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = time.year + "年" + minue + "月";
        return timeCount;
    }

    public String showTimeCountAll(CustomDate time) {
        String timeCount;
        long minuec = time.month;
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());
        long secc = time.day;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = time.year + minue + sec;
        return timeCount;
    }

}