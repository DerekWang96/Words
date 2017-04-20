package com.example.words.aty;

import android.app.Activity;
import com.example.words.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import db.ACID;
import db.Word;


/**
 * 查单词Activity，由任何界面上的查询按钮启动
 * Created by 6gold on 2017/3/15.
 */

public class SearchWordActivity extends Activity implements View.OnClickListener {

    /*成员变量--------------------------------------------------------*/
    private ImageButton btnReturn;

    private EditText etInputWord;
    private ImageButton btnSearchWord;

    private TextView tvWordNoResult;

    private TextView tvWordSpelling;
    private TextView tvWordSoundmark;
    private ImageButton btnWordSpeaker;
    private TextView tvWordMeaning;

    private TextView tvWordExample;

    private Button btnAddWordToList;


    /*相关函数--------------------------------------------------------*/
    //onCreate函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_searchword);

        initViews();//初始化界面
        initEvents();//初始化事件
//        initDatas();//初始化数据
    }

    public void initViews() {
        btnReturn = (ImageButton) findViewById(R.id.btn_return);

        etInputWord = (EditText) findViewById(R.id.et_input_word);
        btnSearchWord = (ImageButton) findViewById(R.id.btn_search_word);

        tvWordNoResult = (TextView) findViewById(R.id.tv_word_no_result);

        tvWordSpelling = (TextView) findViewById(R.id.tv_word_spelling);
        tvWordSoundmark = (TextView) findViewById(R.id.tv_word_soundmark);
        btnWordSpeaker = (ImageButton) findViewById(R.id.btn_word_speaker);
        tvWordMeaning = (TextView) findViewById(R.id.tv_word_meaning);

        tvWordExample = (TextView) findViewById(R.id.tv_word_example);

        btnAddWordToList = (Button) findViewById(R.id.btn_add_word_to_list);

        //首先将查词结果不可见
        clearView();

    }

    //将查词结果不可见
    public void clearView() {
        tvWordNoResult.setVisibility(View.INVISIBLE);
        tvWordSpelling.setVisibility(View.INVISIBLE);
        tvWordSoundmark.setVisibility(View.INVISIBLE);
        btnWordSpeaker.setVisibility(View.INVISIBLE);
        tvWordMeaning.setVisibility(View.INVISIBLE);
        tvWordExample.setVisibility(View.INVISIBLE);
        btnAddWordToList.setVisibility(View.INVISIBLE);
    }

    public void initEvents() {
        btnReturn.setOnClickListener(this);
        btnSearchWord.setOnClickListener(this);
        btnWordSpeaker.setOnClickListener(this);
        btnAddWordToList.setOnClickListener(this);
    }

    //onClick监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_return://返回按钮
            {
                finish();
                break;
            }
            case R.id.btn_search_word://搜词
            {
                clearView();
                //获取搜索框内容
                String wordToSearch = etInputWord.getText().toString();
                //返回
                ACID acid = new ACID(SearchWordActivity.this);
                Word displayWord = acid.searchword(wordToSearch);
                if (displayWord.getExample() == null) {
                    tvWordNoResult.setVisibility(View.VISIBLE);
                } else {
                    tvWordSpelling.setText(displayWord.getSpelling());
                    tvWordSoundmark.setText(displayWord.getSoundmark());
                    tvWordMeaning.setText(displayWord.getParaphrase());
                    tvWordExample.setText(displayWord.getExample());

                    tvWordSpelling.setVisibility(View.VISIBLE);
                    tvWordSoundmark.setVisibility(View.VISIBLE);
                    btnWordSpeaker.setVisibility(View.VISIBLE);
                    tvWordMeaning.setVisibility(View.VISIBLE);
                    tvWordExample.setVisibility(View.VISIBLE);
                    btnAddWordToList.setVisibility(View.VISIBLE);
                }
                break;
            }
            case R.id.btn_word_speaker://发音
            {
                break;
            }
            case R.id.btn_add_word_to_list://添加到单词本
            {
                break;
            }
            default:
                break;
        }
    }
}
