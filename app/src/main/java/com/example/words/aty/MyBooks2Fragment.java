package com.example.words.aty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.R;

import java.util.ArrayList;
import java.util.List;

import db.ACID;
import db.Wordbook;

/**
 * Created by 6gold on 2017/5/22.
 */

public class MyBooks2Fragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {

//    /*数据--------------------------------------------------------*/
//    private ArrayList<String> bookTitles = new ArrayList<String>();         //单词本——标题
//    private ArrayList<String> bookAuthors = new ArrayList<String>();        //单词本——作者
//    private ArrayList<Bitmap> bookCovers = new ArrayList<Bitmap>();         //单词本——封面
//    private ArrayList<Integer> bookWordNumbers = new ArrayList<Integer>();   //单词本——词汇量

    /*成员变量--------------------------------------------------------*/
    View view;
    Context context;

    //查询结果ListView
    private ListView lvMybooksList2;
    private ListItemAdapter listItemAdapter;

    //单词本列表数据源
    List<Wordbook> wordbookList;

    /*相关函数--------------------------------------------------------*/

    /*@重写onCreateView()方法
     * 方法名：onCreate(......)
     * 功    能：创建活动
     * 参    数：......
     * 返回值：无
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_mybooks2,container,false);

        /*初始化操作--------------------------------------------------------*/
        context = this.getActivity();

        initViews();
        initEvents();
        initDatas();

        return view;
    }

    /*
     * 方法名：initViews()
     * 功    能：初始化控件
     * 参    数：无
     * 返回值：无
     */
    public void initViews(){
        lvMybooksList2 = (ListView) view.findViewById(R.id.lv_mybooks_2);
    }

    /*
     * 方法名：initDatas()
     * 功    能：初始化数据(包括适配器的初始化)
     * 参    数：无
     * 返回值：无
     */
    public void initDatas(){

        ACID acid = new ACID(context);
        wordbookList = acid.getwordbooklist("收藏");

        System.out.println("MyBooks2:"+wordbookList.size()+"本");

        listItemAdapter = new ListItemAdapter(wordbookList,this.getActivity());
        lvMybooksList2.setAdapter(listItemAdapter);
    }

    /*
     * 方法名：initEvents()
     * 功    能：初始化事件(点击事件等)
     * 参    数：无
     * 返回值：无
     */
    public void initEvents(){
        lvMybooksList2.setOnItemClickListener(this);
    }


    /*@重写OnItemClickListener()方法
     * 方法名：OnItemClickListener(......)
     * 功    能：处理点击Item事件，此处简单toast它的index
     * 参    数：View v - 被点击的View
     * 返回值：无
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),BookDetailActivity.class);
        intent.putExtra("wordbook",wordbookList.get(position));
        startActivity(intent);
    }

    /*
       * 方法名：Bytes2Bitmap(byte[] b)
       * 功    能：图片格式转换-Bytes转Bitmap
       * 参    数：byte[] b - byte格式的图片
       * 返回值：无
       */
    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /*相关类--------------------------------------------------------*/

    //ListItemAdapter类
    public class ListItemAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<Wordbook> wordbookList;

        public ListItemAdapter(List<Wordbook> list,Context context){
            inflater = LayoutInflater.from(context);
            wordbookList = list;
        }

        @Override
        public int getCount() {
            return wordbookList.size();
        }

        @Override
        public Object getItem(int position) {
            return wordbookList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            System.out.println("getView() in MyBooksFrag_2 used.");

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_book2,null);
                viewHolder = new ViewHolder();
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_booklist2_title);
                viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_booklist2_publisher);
                viewHolder.tvWordNumber = (TextView) convertView.findViewById(R.id.tv_booklist2_amount);
                viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.iv_booklist2_cover);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvTitle.setText(wordbookList.get(position).getName());
            viewHolder.tvAuthor.setText(wordbookList.get(position).getAuthor());
            viewHolder.tvWordNumber.setText(String.valueOf(wordbookList.get(position).getWordnumber()));
            viewHolder.ivCover.setImageBitmap(Bytes2Bitmap(wordbookList.get(position).getPicture()));

            return convertView;
        }
    }

    //ViewHolder类
    public class ViewHolder {
        private ImageView ivCover;
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvWordNumber;
    }
}
