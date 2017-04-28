package com.example.words.aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.words.R;

/**
 * Created by 6gold on 2017/4/28.
 */

public class BookDetailActivity extends Activity {

    /*成员变量--------------------------------------------------------*/
    private ImageButton btn_back;           //返回上层
    private ImageView iv_bookCover;         //单词本封面
    private TextView tv_bookTitle;          //单词本标题
    private TextView tv_bookPublisher;      //发布者
    private TextView tv_wordsAmount;        //词汇量
    private ImageButton btn_addFavorite;    //收藏按钮
    private TextView tv_favoriteNum;        //收藏量
    private ImageButton btn_download;       //下载按钮
    private TextView tv_downloadNum;        //下载量
    private ImageButton btn_review;         //复习
    private ImageButton btn_learn;          //学习
    private ImageButton btn_test;           //测试
    //单词描述

    /*
     * 方法名：initViews()
     * 功    能：初始化控件
     * 参    数：无
     * 返回值：无
     */
    private void initViews() {
        btn_back = (ImageButton) findViewById(R.id.btn_return_searchbook);
        iv_bookCover = (ImageView) findViewById(R.id.iv_bookdetail_cover);
        tv_bookTitle = (TextView) findViewById(R.id.tv_bookdetail_title);
        tv_bookPublisher = (TextView) findViewById(R.id.tv_bookdetail_publisher);
        tv_wordsAmount = (TextView) findViewById(R.id.tv_bookdetail_amount);
        btn_addFavorite = (ImageButton) findViewById(R.id.btn_bookdetail_addFavorite);
        tv_favoriteNum = (TextView) findViewById(R.id.tv_bookdetail_favoriteNum);
        btn_download = (ImageButton) findViewById(R.id.btn_bookdetail_download);
        tv_downloadNum = (TextView) findViewById(R.id.tv_bookdetail_downloadNum);
        btn_review = (ImageButton) findViewById(R.id.btn_bookdetail_review);
        btn_learn = (ImageButton) findViewById(R.id.btn_bookdetail_learn);
        btn_test = (ImageButton) findViewById(R.id.btn_bookdetail_test);
    }

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);      //去掉头部
        setContentView(R.layout.activity_bookdetail);       //设置layotu
        initViews();    //初始化控件
//        initEvents();   //初始化事件
//        initDatas();    //初始化数据
    }

}
