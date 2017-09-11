package com.example.words.aty;

import android.app.Activity;
import com.example.words.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import db.ACID;
import db.Word;
import db.Wordbook;


/**
 * 查单词Activity，由任何界面上的查询按钮启动
 * Created by 6gold on 2017/3/15.
 */

public class SearchWordActivity extends Activity implements View.OnClickListener {

    /*成员变量--------------------------------------------------------*/
    private Context mContext;
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

    private Intent intent = null;//用于截取要查询的单词

    private List<Wordbook> wordbooklist;//自定义单词本的列表
    private String[] wordbooknames ;
//    private String[] list = {"a","b","c","d","e",
//            "f","g","h","i","j","k","l","m","n","o"};
    private boolean[] checks;//存放checkbox的结果

    private SearchWordActivity.MyAdapter myAdapter;

    private LinearLayout llCreateNewWordBook;
    private Button btnFinishSelecing;


    /*相关函数--------------------------------------------------------*/
    //onCreate函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_searchword);

        initDatas();//初始化数据
        initViews();//初始化界面
        initEvents();//初始化事件

    }

    public void initDatas() {
        intent = getIntent();
        myAdapter = new SearchWordActivity.MyAdapter(this);
        wordbooklist = new ACID(this).getwordbooklist(null);
        wordbooknames = new String[wordbooklist.size()];
        for(int i=0;i<wordbooklist.size();i++){
            wordbooknames[i] = wordbooklist.get(i).getName();
        }
        checks = new boolean[wordbooknames.length];
    }

    public void initViews() {
        mContext = SearchWordActivity.this;
        btnReturn = (ImageButton) findViewById(R.id.btn_return_searchbook);

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
        String s = intent.getStringExtra("haswordtocheck");
        System.out.println(s);
        if (!s.equals("noword")) {
            String wordToCheck = intent.getStringExtra("wordToCheck");
            searchWord(wordToCheck);
        }

        btnReturn.setOnClickListener(this);
        btnSearchWord.setOnClickListener(this);
        btnWordSpeaker.setOnClickListener(this);
        btnAddWordToList.setOnClickListener(this);
    }

    //onClick监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_return_searchbook://返回按钮
            {
                finish();
                break;
            }
            case R.id.btn_search_word://搜词
            {
                //获取搜索框内容
                String wordToSearch = etInputWord.getText().toString();
                searchWord(wordToSearch);
                break;
            }
            case R.id.btn_word_speaker://发音
            {
                break;
            }
            case R.id.btn_add_word_to_list://添加到单词本
            {
                View popupView = SearchWordActivity.this.getLayoutInflater().
                        inflate(R.layout.ppwindow_select_book, null);
                ListView lvWordbooks = (ListView) popupView.findViewById(R.id.lv_select_from_mybooks);
                lvWordbooks.setAdapter(myAdapter);

                PopupWindow window = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,800);
//                window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//                window.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                // 设置动画
                window.setAnimationStyle(R.style.popup_window_anim);
                // 设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                // 设置可以获取焦点
                window.setFocusable(true);
                // 设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(true);
                // 更新popupwindow的状态
                window.update();
                // 以下拉的方式显示，并且可以设置显示的位置
//                window.showAsDropDown(btnAddWordToList, 0, 20);
                // 以上拉的方式显示
                window.showAtLocation(findViewById(R.id.ll_search_word),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);

                llCreateNewWordBook = (LinearLayout) popupView.findViewById(R.id.ll_create_new_wordbook);
                llCreateNewWordBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchWordActivity.this,CreateBookActivity.class);
                        startActivityForResult(intent,1);//新建单词本后需返回给当前Aty
                    }
                });

                btnFinishSelecing = (Button) popupView.findViewById(R.id.btn_finish_selecting_books);
                btnFinishSelecing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("添加添加添加添加添加添加添加添加");
                        String wordToAdd = tvWordSpelling.getText().toString();
                        System.out.println("wordToAdd:"+wordToAdd);
                        ACID acid = new ACID(mContext);
                        for(int i = 0;i<checks.length;i++){
                            if (checks[i]) {
                                System.out.println(wordbooknames[i]+"    "+wordToAdd);
                                acid.addword(wordbooknames[i],wordToAdd);
                            }
                        }
                    }
                });

                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if (resultCode == 1) {
                //用新的单词本数组和新的bool数组替换掉原来的
                String newbookname = data.getStringExtra("newbookname");
                if (!newbookname.equals("")){
                    System.out.println("新单词本："+newbookname);
                    int length = wordbooknames.length;
                    String[] tempwordbooknames = new String[length+1];
                    for (int i = 0;i<length;i++){
                        tempwordbooknames[i] = wordbooknames[i];
                    }
                    tempwordbooknames[length] = newbookname;
                    wordbooknames = tempwordbooknames;

                    boolean[] tempchecks = new boolean[length+1];
                    for (int i = 0;i<length;i++){
                        tempchecks[i] = checks[i];
                    }
                    tempchecks[length] = false;
                    checks = tempchecks;
                    myAdapter.notifyDataSetChanged();//刷新数据源
                }
            }
    }

    public void searchWord(String wordToSearch) {
        clearView();
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
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;
        private MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return wordbooknames.length;
        }

        @Override
        public Object getItem(int position) {
            return wordbooknames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SearchWordActivity.ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new SearchWordActivity.ViewHolder();
                convertView = mInflater.inflate(R.layout.wordlist_item,null);
                viewHolder.tvBookName = (TextView) convertView.findViewById(R.id.tv_wordlist_word);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_check_word);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (SearchWordActivity.ViewHolder) convertView.getTag();
            }
//            viewHolder.tvBookName.setText(list.get(position).getName());
            viewHolder.tvBookName.setText(wordbooknames[position]);
            final int pos = position;
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checks[pos] = isChecked;
                }
            });
            viewHolder.checkBox.setChecked(checks[pos]);

            return convertView;
        }
    }

    public class ViewHolder {
        private TextView tvBookName;
        private CheckBox checkBox;

        public ViewHolder(){}
    }
}
