package com.example.words.aty;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.words.R;
import com.example.words.util.RecycleViewDivider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.ACID;
import db.Word;

public class DisplayWordsActivity2 extends Activity implements View.OnClickListener {


    //控件
    private ImageButton btnReturn;
    private Button btnManage;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter2 adapter;

    //隐藏控件
    private LinearLayout llDisplayWordsManage;
    public TextView tvDeleteNumber;
    private Button btnDelete;


    //每一行的填充数据
    String wordBookName;
    private List<Word> wordslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_words2);

        initDatas();
        initViews();
        initEvents();

        recyclerView = (RecyclerView) findViewById(R.id.rv_manage_words);
        System.out.println("rv:"+recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter2(wordslist);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this,LinearLayoutManager.VERTICAL,R.drawable.recyclerview_divider));

        // 为RecycleView绑定触摸事件
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 首先回调的方法 返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;//拖拽
                int swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;//侧滑删除
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 滑动事件
                Collections.swap(wordslist,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑事件
                wordslist.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    //初始化数据
    protected void initDatas() {
        Intent intent = getIntent();
        ACID acid = new ACID(DisplayWordsActivity2.this);
        wordBookName = intent.getStringExtra("wordbookName");//获取单词本名字

        wordslist = acid.getwordfromwb(intent.getStringExtra("wordbookName"),null);//初始化单词本
    }

    //初始化界面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void initViews() {

        btnReturn = (ImageButton) findViewById(R.id.btn_return2);
        btnManage = (Button) findViewById(R.id.btn_help2);
        tvTitle = (TextView) findViewById(R.id.tv_topbar_title2);

        llDisplayWordsManage = (LinearLayout) findViewById(R.id.ll_display_words_manage);
        tvDeleteNumber = (TextView) findViewById(R.id.tv_number_of_words_to_delete);
        btnDelete = (Button) findViewById(R.id.btn_delete_words);

        tvTitle.setText(wordBookName);
    }

    //初始化事件
    protected void initEvents() {
        btnReturn.setOnClickListener(this);
        btnManage.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_return2:
            {
//                ACID acid = new ACID(DisplayWordsActivity2.this);
//                acid.updateOrder(wordslist);              //退出之前更新单词本内单词的顺序
                finish();
            }

            case R.id.btn_help2:{
                if (btnManage.getText().toString().equals("管理")) {
                    btnManage.setText("退出管理");
                    //取消隐藏
                    showHiddenItems(new Boolean("true"));
                } else if (btnManage.getText().toString().equals("退出管理")) {
                    btnManage.setText("管理");
                    //隐藏
                    showHiddenItems(new Boolean("false"));
                }
                break;
            }

            case R.id.btn_delete_words:
            {
                int count =  0;
                List<Boolean> flags = adapter.getChecks();
                int length = adapter.getList().size();
                for (int i = 0;i < length ;i++) {
                    for (int j = 0 ; j < adapter.getList().size() ; j++){
                        if (flags.get(j)) {
                            wordslist.remove(j);
                            flags.remove(j);
                            count++;
                            System.out.println("i:"+i+"; j:"+j+"; listsize:"+adapter.getList().size()+"; count:"+count );
                            adapter.setList(wordslist);
                            adapter.setChecks(flags);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            break;
                        }
                    }
                }

                break;
            }

        }
    }

    public void showHiddenItems(Boolean b) {
        if (b) {
            //取消隐藏
            llDisplayWordsManage.setVisibility(View.VISIBLE);
            //显示checkBox
            for (int i = 0; i < adapter.getList().size(); i++) {
                View view = recyclerView.getChildAt(i);
                RecyclerViewAdapter2.MyViewHolder holder = (RecyclerViewAdapter2.MyViewHolder) recyclerView.getChildViewHolder(view);
                holder.checkBox.setVisibility(View.VISIBLE);
            }
        } else {
            //隐藏
            llDisplayWordsManage.setVisibility(View.GONE);
            //隐藏checkBox
            for (int i = 0; i < adapter.getList().size(); i++) {
                View view = recyclerView.getChildAt(i);
                RecyclerViewAdapter2.MyViewHolder holder = (RecyclerViewAdapter2.MyViewHolder) recyclerView.getChildViewHolder(view);
                System.out.println("调用showHiddenItems函数里的checkBox.setVisibility(View.GONE)");
                holder.checkBox.setVisibility(View.GONE);
            }
        }
    }

    //适配器
    public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.MyViewHolder> {

        private List<Word> list;
        private List<Boolean> checks;
        private Context context;

        public RecyclerViewAdapter2(List<Word> list) {
            this.list = list;
            checks = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                checks.add(new Boolean("flase"));
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvWord;
            public CheckBox checkBox;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvWord = (TextView) itemView.findViewById(R.id.tv_wordlist_item2_word);
                checkBox = (CheckBox) itemView.findViewById(R.id.cb_pick_words_to_delete);
                System.out.println("调用ViewHolder构造函数里的checkBox.setVisibility(View.GONE)");
                checkBox.setVisibility(View.GONE);//checkbox默认隐藏
            }
        }

        @Override
        public RecyclerViewAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlist_item2, parent, false);
            return new RecyclerViewAdapter2.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter2.MyViewHolder holder, final int position) {
            Word word = list.get(position);

            holder.tvWord.setText(word.getSpelling());

            final int pos = position;//********
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checks.set(position,isChecked);
                }
            });//********
            holder.checkBox.setChecked(checks.get(pos));
            tvDeleteNumber.setText(String.valueOf(checks.size()));
            //********
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<Word> list) {
            this.list = list;
        }

        public void setChecks(List<Boolean> checks) {
            this.checks = checks;
        }

        public List<Word> getList() {
            return list;
        }

        public List<Boolean> getChecks() {
            return checks;
        }


    }

}

