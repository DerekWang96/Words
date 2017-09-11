package com.example.words.aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import db.ACID;
import db.Word;

public class MemorizeWordsActivity extends Activity implements View.OnClickListener {

    private Intent intent;              //截取前一个Aty传来的数据
    private String wordbookTitle;

    private Context context;
    private TextView tvClickToSee;
    private TextView tvWordSpelling;    //单词拼写
    private TextView tvWordSoundmark;   //单词音标
    private TextView contentPart1;      //单词释义
    private TextView tvWordExamples;    //单词例句
    private ScrollView contentPart2;
    private LinearLayout contentPart3;
    private Button btnYes,btnNotSure,btnNo;
    private int memoryCount = 0;

    private List<Word> wordlist;        //单词数据
    private int progress = 0;           //当前进度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize_words);
        context = this;

        //初始化
        initDatas();
        initViews();
        initEvents();

        setDefaultView();
        setContentView();

        setWord();
    }

    public void initDatas() {
        intent = getIntent();
        wordbookTitle = intent.getStringExtra("wordbookTitle");
        System.out.println("title:"+wordbookTitle);
        wordlist = new ACID(context).getword(wordbookTitle);//添加参数：读取数量
    }

    private void initViews() {

        tvClickToSee = (TextView) findViewById(R.id.tv_click_to_see);

        tvWordSpelling = (TextView) findViewById(R.id.tv_memorize_spelling);
        tvWordSoundmark = (TextView) findViewById(R.id.tv_memorize_soundmark);
        tvWordExamples = (TextView) findViewById(R.id.tv_memorize_examples);

        contentPart1 = (TextView) findViewById(R.id.memorize_content_part_1);
        contentPart2 = (ScrollView) findViewById(R.id.memorize_content_part_2);
        contentPart3 = (LinearLayout) findViewById(R.id.memorize_content_part_3);

        btnYes = (Button) findViewById(R.id.btn_memorize_yes);
        btnNotSure = (Button) findViewById(R.id.btn_memorize_not_sure);
        btnNo = (Button) findViewById(R.id.btn_memorize_no);
    }

    private void initEvents() {
        tvClickToSee.setOnClickListener(this);
        btnYes.setOnClickListener(this);
        btnNotSure.setOnClickListener(this);
        btnNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_click_to_see:
            {
                hideDefaultView();
                contentPart1.setVisibility(View.VISIBLE);
                contentPart2.setVisibility(View.VISIBLE);
                contentPart3.setVisibility(View.VISIBLE);
                break;
            }

            case R.id.btn_memorize_yes:
            {
                memoryCount++;
                if (memoryCount < wordlist.size()) {
                    setDefaultView();
                }
                else {
                    ACID newACID = new ACID(context);
                    newACID.addintomywords(wordlist);
                }
                System.out.println("count:"+progress);
                int temp = wordlist.get(progress).getFamilarity();
                switch(temp){
                    case 0:
                        wordlist.get(progress).setFamilarity(3);
                        break;
                    case 1:
                        wordlist.get(progress).setFamilarity(3);
                        break;
                    case 2:
                        wordlist.get(progress).setFamilarity(3);
                        break;
                    default:
                        break;
                }

                if (memoryCount<wordlist.size()) {
                    setWord();
                    break;
                }
                else {
                    Toast.makeText(getApplicationContext(), "已完成今日学习任务",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            case R.id.btn_memorize_not_sure:
            {
                setDefaultView();
                int temp = wordlist.get(progress).getFamilarity();
                switch(temp){
                    case 0:
                        wordlist.get(progress).setFamilarity(1);
                        break;
                    case 1:
                        wordlist.get(progress).setFamilarity(2);
                        break;
                    case 2:
                        wordlist.get(progress).setFamilarity(2);
                        break;
                    default:
                        break;
                }
                if (memoryCount<wordlist.size()) setWord();
                break;
            }

            case R.id.btn_memorize_no:
            {
                setDefaultView();
                int temp = wordlist.get(progress).getFamilarity();
                switch(temp){
                    case 0:
                        wordlist.get(progress).setFamilarity(1);
                        break;
                    case 1:
                        wordlist.get(progress).setFamilarity(1);
                        break;
                    case 2:
                        wordlist.get(progress).setFamilarity(2);
                        break;
                    default:
                        break;
                }
                if (memoryCount<wordlist.size()) setWord();
                break;
            }
        }
    }

    public void setDefaultView() {
        tvClickToSee.setVisibility(View.VISIBLE);
    }

    public void hideDefaultView() {
        tvClickToSee.setVisibility(View.GONE);
    }

    public void setContentView() {
        contentPart1.setVisibility(View.VISIBLE);
        contentPart2.setVisibility(View.VISIBLE);
        contentPart3.setVisibility(View.VISIBLE);
    }

    public void setWord() {
        System.out.println(wordlist.size());
        progress = (progress+1)%wordlist.size();
        Word temp = wordlist.get(progress);
        if(temp.getFamilarity() == 3){
//            progress = (progress+1)%wordlist.size();
            setWord();
        }
        else {
            tvWordSpelling.setText(temp.getSpelling());
            tvWordSoundmark.setText(temp.getSoundmark());
            contentPart1.setText(temp.getMeaning());
            tvWordExamples.setText(temp.getExample());
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ACID newACID = new ACID(context);
        newACID.addintomywords(wordlist);
    }
}