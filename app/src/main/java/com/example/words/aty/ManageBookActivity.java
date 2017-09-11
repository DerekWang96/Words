package com.example.words.aty;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.words.R;
import com.example.words.util.BitmapByte;
import com.example.words.util.RecycleViewDivider;
import com.example.words.util.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.Wordbook;
import db.ACID;

public class ManageBookActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private ImageButton btnReturn;
    private Button btnHelp, btnDeleteBooks;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Wordbook> list;
    boolean isAllChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_book);

        initViews();
        initDatas();
        initEvents();
        recyclerView = (RecyclerView) findViewById(R.id.rv_manage_book);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(list);
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
                Collections.swap(list,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑事件
                list.remove(viewHolder.getAdapterPosition());
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

    private void initViews() {
        title = (TextView) findViewById(R.id.tv_topbar_title);
        title.setText("管理我的单词本");
        btnReturn = (ImageButton) findViewById(R.id.btn_return);
        btnHelp = (Button) findViewById(R.id.btn_help);

        btnHelp.setText("全选");

        btnDeleteBooks = (Button) findViewById(R.id.btn_delete_wordbooks);

    }

    // 初始化数据
    private void initDatas() {
        ACID acid = new ACID(ManageBookActivity.this);
        list = acid.getwordbooklist("下载");
    }

    private void initEvents() {
        btnReturn.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        btnDeleteBooks.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_return:
            {
                ACID acid = new ACID(ManageBookActivity.this);
                acid.updateOrder(list);
                finish();
            }

            case R.id.btn_help:
            {
                //全选
                if (isAllChecked) {
                    isAllChecked = false;
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        adapter.setItemChecked(i,false);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    isAllChecked = true;
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        adapter.setItemChecked(i,true);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            }

            case R.id.btn_delete_wordbooks:
            {
                int count =  0;
                List<Boolean> flags = adapter.getChecks();
                int length = adapter.getList().size();
                for (int i = 0;i < length ;i++) {
                    for (int j = 0 ; j < adapter.getList().size() ; j++){
                        if (flags.get(j)) {
                            list.remove(j);
                            flags.remove(j);
                            count++;
                            System.out.println("i:"+i+"; j:"+j+"; listsize:"+adapter.getList().size()+"; count:"+count );
//                        adapter.notifyItemRemoved(i);
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }

                //删除选中的单词本
//                int length = adapter.getList().size();
//                for (int j = 0; j < length; j++) {
//                    for (int i = 0; i < flags.size() ; i++) {
//                        if (flags.get(i)) {
//                            System.out.println("i "+ i);
//                            list.remove(i - count);
////                            flags.set(i, false);
//                            flags.remove(i - count);
//                            adapter.notifyItemRemoved(i - count);
//                            adapter.setChecks(flags);
//                            adapter.notifyDataSetChanged();
//                            System.out.println(++count);
//
//                            break;
//                        }
//                    }
//                }
                break;
            }
        }
    }
}
