package com.example.words.aty;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.R;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;

import db.ACID;
import db.Word;

public class DisplayWordsActivity extends Activity implements View.OnClickListener {


    //控件
    private SlideAndDragListView slideAndDragListView;
    private ImageButton btnReturn;
    private TextView tvTitle;
    private TextView tvDeleteNumber;
    private Button btnDelete;

    //隐藏菜单
    Menu menu;

    //每一行的填充数据
    Word mDraggedEntity;
    private List<Word> wordslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_words);

        initDatas();
        initMenus();
        initViews();
        initEvents();
    }

    //初始化数据
    protected void initDatas() {
        Intent intent = getIntent();
        ACID acid = new ACID(DisplayWordsActivity.this);
        wordslist = acid.getwordfromwb(intent.getStringExtra("wordbookName"),null);//初始化单词本
    }

    //初始化隐藏菜单
    protected void initMenus() {
        //初始化隐藏菜单
        menu = new Menu(true,0);
        menu.addItem(new MenuItem.Builder().setWidth(200)
                        .setBackground(getResources().getDrawable(R.drawable.test_btn_shape1))
                        .setText("删除")
                        .setTextColor(R.color.colorTextDark)
                        .setTextSize(16).setDirection(MenuItem.DIRECTION_RIGHT)
                        .build());
    }

    //初始化界面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void initViews() {

        btnReturn = (ImageButton) findViewById(R.id.btn_return);
        tvTitle = (TextView) findViewById(R.id.tv_topbar_title);
        tvDeleteNumber = (TextView) findViewById(R.id.tv_number_of_words_to_delete);
        btnDelete = (Button) findViewById(R.id.btn_delete_words);

        tvTitle.setText("单词本名字");

        slideAndDragListView = (SlideAndDragListView) findViewById(R.id.sdlv);
        slideAndDragListView.setMenu(menu);//设置隐藏菜单
        slideAndDragListView.setAdapter(mAdapter);//设置适配器
    }

    //初始化事件
    protected void initEvents() {

        btnReturn.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        /*设置各种监听器*/

        //隐藏菜单子项点击监听
        slideAndDragListView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View view, int itemPosition, int buttonPosition, int direction) {
                switch (direction) {
                    //若打开左边的item
                    case MenuItem.DIRECTION_LEFT:
                        break;

                    //若打开右边的item
                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 0:
                                //从底部到顶部删除listView的子项，选择这个会调用
                                // setOnItemDeleteListener的onItemDelete方法
                                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                        }
                        break;
                    default:
                        return Menu.ITEM_NOTHING;
                }
                return Menu.ITEM_NOTHING;
            }
        });

        //设置移动控件监听
        slideAndDragListView.setOnDragDropListener(new SlideAndDragListView.OnDragDropListener() {
            @Override
            public void onDragViewStart(int beginPosition) {
                mDraggedEntity = wordslist.get(beginPosition);
            }

            @Override
            public void onDragDropViewMoved(int fromPosition, int toPosition) {
                Word movedword = wordslist.remove(fromPosition);
                wordslist.add(toPosition, movedword);
            }

            @Override
            public void onDragViewDown(int finalPosition) {
                wordslist.set(finalPosition, mDraggedEntity);
            }
        });

        //设置点击监听
        slideAndDragListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(DisplayWordsActivity.this, "点击项目" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DisplayWordsActivity.this,SearchWordActivity.class);
                intent.putExtra("wordToCheck",wordslist.get(position));
                startActivity(intent);
            }
        });

        //设置滑动监听
        slideAndDragListView.setOnSlideListener(new SlideAndDragListView.OnSlideListener() {
            @Override
            public void onSlideOpen(View view, View view1, int position, int direction) {
                Toast.makeText(DisplayWordsActivity.this, "滑动打开监听" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSlideClose(View view, View view1, int position, int direction) {
                Toast.makeText(DisplayWordsActivity.this, "滑动关闭监听" + position, Toast.LENGTH_SHORT).show();
            }
        });

        //设置删除Item监听
        slideAndDragListView.setOnItemDeleteListener(new SlideAndDragListView.OnItemDeleteListener() {
            @Override
            public void onItemDeleteAnimationFinished(View view, int position) {
                Toast.makeText(DisplayWordsActivity.this, "删除" + position, Toast.LENGTH_SHORT).show();
                wordslist.remove(position - slideAndDragListView.getHeaderViewsCount());
                mAdapter.notifyDataSetChanged();
            }
        });

        //设置滚动监听
        slideAndDragListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            //滑动状态改变监听
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                    case SCROLL_STATE_FLING:
                        break;
                }
            }

            @Override
            //当滚动的时候触发
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //会一直调用
                //Toast.makeText(MainActivity.this, "正在滚动" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    //适配器类
    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return wordslist.size();
        }

        @Override
        public Object getItem(int position) {
            return wordslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                //加载布局
                convertView = LayoutInflater.from(DisplayWordsActivity.this).inflate(R.layout.wordlist_item,null);
                //初始化控件
                vh.checkBox = (CheckBox) convertView.findViewById(R.id.cb_check_word);
                vh.word = (TextView) convertView.findViewById(R.id.tv_wordlist_word);
                //设置checkbox选中监听

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            //得到某行单词
            String item = ((Word)this.getItem(position)).getSpelling().toString();

            //设置控件
            vh.word.setText(item);
            return convertView;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_return:
                finish();
                break;
            case R.id.btn_delete_words:
                //删除单词
                break;
        }
    }

    private class ViewHolder {
        public CheckBox checkBox;
        public TextView word;
    }

    public class TestAdapter extends ArrayAdapter<String> {
        int resource;
        private LayoutInflater inflater;
        private Boolean[] checks;//保存CheckBox的状态

        public TestAdapter(Context context,int resource,ArrayList<String> list) {
            super(context,resource,list);
            checks = new Boolean[list.size()];
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }
}
