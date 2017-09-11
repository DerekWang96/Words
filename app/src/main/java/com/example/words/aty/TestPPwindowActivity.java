package com.example.words.aty;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.words.R;

import db.ACID;
import db.Word;

/**
 * Created by 6gold on 2017/8/13.
 */

public class TestPPwindowActivity extends Activity implements View.OnClickListener{
    /*成员变量--------------------------------------------------------*/

    private Button btnAddWordToList;


    //    private List<Wordbook> list;//自定义单词本的列表
    private String[] list = {"a","b","c","d","e",
            "f","g","h","i","j","k","l","m","n","o"};
    private boolean[] checks = new boolean[list.length];//存放checkbox的结果

    private MyAdapter myAdapter;


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
        myAdapter = new MyAdapter(this);
    }

    public void initViews() {
        btnAddWordToList = (Button) findViewById(R.id.btn_add_word_to_list);
    }


    public void initEvents() {
        btnAddWordToList.setOnClickListener(this);
    }

    //onClick监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_word_to_list://添加到单词本
            {
                View popupView = TestPPwindowActivity.this.getLayoutInflater().
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
                break;
            }
            default:
                break;
        }
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;
        private MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TestPPwindowActivity.ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.wordlist_item,null);
                viewHolder.tvBookName = (TextView) convertView.findViewById(R.id.tv_wordlist_word);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_check_word);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (TestPPwindowActivity.ViewHolder) convertView.getTag();
            }
//            viewHolder.tvBookName.setText(list.get(position).getName());
            viewHolder.tvBookName.setText(list[position]);
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
