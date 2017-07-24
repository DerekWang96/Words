package com.example.words.aty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.words.AsyncMsgRes;
import com.example.words.ExDataTask;
import com.example.words.MessagePack;
import com.example.words.R;
import com.example.words.util.BitmapByte;

import db.Wordbook;

/**
 * Created by 6gold on 2017/4/28.
 */

public class BookDetailActivity extends Activity implements View.OnClickListener ,AsyncMsgRes{

    /*数据--------------------------------------------------------*/
    Wordbook wordbook;

//    String bookTitle,bookAuthor;                            //单词本书名和作者
//    int bookWordNumber,bookFavNumber,bookDownNumber;     //词汇量，收藏量，下载量
//    Bitmap bookCover;                                       //单词本封面
    Boolean f = false,d = false;                            //记录当前用户是否收藏或者下载该单词本，初始化为false
    Intent intent0;//截取前一个aty传递的数据

    /*成员变量--------------------------------------------------------*/
    private ImageButton btn_back;           //返回上层
    private ImageView iv_bookCover;         //单词本封面
    private TextView tv_bookTitle;          //单词本标题
    private TextView tv_bookAuthor;      //发布者
    private TextView tv_wordsAmount;        //词汇量
    private ImageButton btn_addFavorite;    //收藏按钮
    private TextView tv_favoriteNum;        //收藏量
    private ImageButton btn_download;       //下载按钮
    private TextView tv_downloadNum;        //下载量
    private ImageButton btn_review;         //复习
    private ImageButton btn_learn;          //学习
    private ImageButton btn_test;           //测试

    private LinearLayout llBookdetailPartA;
//    private TextView bookDescription;       //单词本描述
//    private TextView learningProgress;      //学习进度
//    private TextView avgWrong;              //平均错误率
//    private TextView maxRight;              //最高正确率
//    private TextView mostWrongWords;        //错误次数最多的词语



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
        initDatas();    //初始化数据
        initViews();    //初始化控件
        initEvents();   //初始化事件
//        initDatas();    //初始化数据
    }

    private void initDatas() {
        intent0 = getIntent();
        wordbook = (Wordbook) intent0.getSerializableExtra("wordbook");

//        bookTitle = wordbook.getName();
//        bookAuthor = wordbook.getAuthor();
//        bookWordNumber = wordbook.getWordnumber();
//        bookFavNumber = wordbook.getCollectnumber();
//        bookDownNumber = wordbook.getDownnumber();
//        bookCover = new BitmapByte().Bytes2Bimap(wordbook.getPicture());
    }

    /*
     * 方法名：initViews()
     * 功    能：初始化控件
     * 参    数：无
     * 返回值：无
     */
    private void initViews() {
        btn_back = (ImageButton) findViewById(R.id.btn_return_bookdetail);
        iv_bookCover = (ImageView) findViewById(R.id.iv_bookdetail_cover);
        tv_bookTitle = (TextView) findViewById(R.id.tv_bookdetail_title);
        tv_bookAuthor = (TextView) findViewById(R.id.tv_bookdetail_publisher);
        tv_wordsAmount = (TextView) findViewById(R.id.tv_bookdetail_amount);
        btn_addFavorite = (ImageButton) findViewById(R.id.btn_bookdetail_addFavorite);
        tv_favoriteNum = (TextView) findViewById(R.id.tv_bookdetail_favoriteNum);
        btn_download = (ImageButton) findViewById(R.id.btn_bookdetail_download);
        tv_downloadNum = (TextView) findViewById(R.id.tv_bookdetail_downloadNum);
        btn_review = (ImageButton) findViewById(R.id.btn_bookdetail_review);
        btn_learn = (ImageButton) findViewById(R.id.btn_bookdetail_learn);
        btn_test = (ImageButton) findViewById(R.id.btn_bookdetail_test);
        llBookdetailPartA = (LinearLayout) findViewById(R.id.ll_bookdetail_partA);

        iv_bookCover.setImageBitmap(new BitmapByte().Bytes2Bitmap(wordbook.getPicture()));
        tv_bookTitle.setText(wordbook.getName());
        tv_bookAuthor.setText(wordbook.getAuthor());
        tv_wordsAmount.setText(String.valueOf(wordbook.getWordnumber()));
        tv_favoriteNum.setText(String.valueOf(wordbook.getCollectnumber()));
        tv_downloadNum.setText(String.valueOf(wordbook.getDownnumber()));
    }

    /*
     * 方法名：initEvents()
     * 功    能：初始化事件(点击事件等)
     * 参    数：无
     * 返回值：无
     */
    public void initEvents() {
        btn_back.setOnClickListener(this);
        btn_addFavorite.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        btn_review.setOnClickListener(this);
        btn_learn.setOnClickListener(this);
        btn_test.setOnClickListener(this);
        llBookdetailPartA.setOnClickListener(this);
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
            case R.id.btn_return_bookdetail:        //返回按钮
            {
                finish();
                break;
            }

            case R.id.btn_bookdetail_addFavorite:   //收藏按钮
            {
                if(f==false) {
                    btn_addFavorite.setBackgroundResource(R.mipmap.btn_favorite);
                    f = true;
                    //开线程修改数据库中的数据
                } else {
                    btn_addFavorite.setBackgroundResource(R.mipmap.btn_unfavorite);
                    f = false;
                    //开线程修改数据库中的数据
                }
                break;
            }

            case R.id.btn_bookdetail_download:      //下载按钮
            {
                if(d==false) {
                    //btn_download.setBackgroundResource(R.mipmap.btn_download);//【待修改】成功之后再修改图标。。。
                    d = true;
                    ExDataTask DTask = new ExDataTask(BookDetailActivity.this);
                    DTask.setOrderType("0001");
                    DTask.setDelegate(BookDetailActivity.this);
                    DTask.setContext(BookDetailActivity.this);
                    DTask.setFilename("test.db");
                    DTask.execute();

                    //开线程修改数据库中的数据
                }
                break;
            }

            case R.id.ll_bookdetail_partA:
            {
                Intent intent = new Intent(BookDetailActivity.this,DisplayWordsActivity.class);
                intent.putExtra("wordbookName",wordbook.getName());
                startActivity(intent);
                //判断本地是否有该单词本，若有，从本地读取，若无，联网读
            }

            case R.id.btn_bookdetail_review:        //复习按钮
            {
                break;
            }

            case R.id.btn_bookdetail_learn:         //学习按钮
            {
                break;
            }

            case R.id.btn_bookdetail_test:          //测试按钮
            {
                break;
            }
        }
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

    @Override
    public void processFinish(MessagePack outputPack) {
        btn_download.setBackgroundResource(R.mipmap.btn_download);//【待修改】成功之后再修改图标。。。
    }
}
